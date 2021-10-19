package kst.app.fcsubwayinfo.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kst.app.fcsubwayinfo.data.api.StationApi
import kst.app.fcsubwayinfo.data.api.StationArrivalsApi
import kst.app.fcsubwayinfo.data.api.response.mapper.toArrivalInformation
import kst.app.fcsubwayinfo.data.db.StationDao
import kst.app.fcsubwayinfo.data.db.entity.StationSubwayCrossRefEntity
import kst.app.fcsubwayinfo.data.db.entity.mapper.toStationEntity
import kst.app.fcsubwayinfo.data.db.entity.mapper.toStations
import kst.app.fcsubwayinfo.data.preference.PreferenceManager
import kst.app.fcsubwayinfo.domain.ArrivalInformation
import kst.app.fcsubwayinfo.domain.Station

class StationRepositoryImpl(
    private val stationArrivalsApi: StationArrivalsApi,
    private val stationApi: StationApi,
    private val stationDao: StationDao,
    private val preferenceManager: PreferenceManager,
    private val dispatcher: CoroutineDispatcher
) : StationRepository {

    override val stations: Flow<List<Station>> =
        stationDao.getStationWithSubways()
            .distinctUntilChanged()
            .map { stations -> stations.toStations().sortedByDescending { it.isFavorited } }
            .flowOn(dispatcher)

    override suspend fun refreshStations() = withContext(dispatcher) {
        val fileUpdatedTimeMillis = stationApi.getStationDataUpdatedTimeMillis()
        val lastDatabaseUpdatedTimeMillis = preferenceManager.getLong(KEY_LAST_DATABASE_UPDATED_TIME_MILLIS)

        if (lastDatabaseUpdatedTimeMillis == null || fileUpdatedTimeMillis > lastDatabaseUpdatedTimeMillis) {
            stationDao.insertStationSubways(stationApi.getStationSubways())
            preferenceManager.putLong(KEY_LAST_DATABASE_UPDATED_TIME_MILLIS, fileUpdatedTimeMillis)
        }
    }

    override suspend fun getStationArrivals(stationName: String): List<ArrivalInformation> = withContext(dispatcher) {
        stationArrivalsApi.getRealtimeStationArrivals(stationName)
            .body()
            ?.realtimeArrivalList
            ?.toArrivalInformation()
            ?.distinctBy { it.direction }
            ?.sortedBy { it.subway }
            ?: throw RuntimeException("도착 정보를 불러오는 데에 실패했습니다.")
    }

    override suspend fun updateStation(station: Station) = withContext(dispatcher) {
        stationDao.updateStation(station.toStationEntity())
    }

    companion object {
        private const val KEY_LAST_DATABASE_UPDATED_TIME_MILLIS = "KEY_LAST_DATABASE_UPDATED_TIME_MILLIS"
    }
}