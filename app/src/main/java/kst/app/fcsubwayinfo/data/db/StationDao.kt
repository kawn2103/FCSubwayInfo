package kst.app.fcsubwayinfo.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kst.app.fcsubwayinfo.data.db.entity.StationEntity
import kst.app.fcsubwayinfo.data.db.entity.StationSubwayCrossRefEntity
import kst.app.fcsubwayinfo.data.db.entity.StationWithSubwaysEntity
import kst.app.fcsubwayinfo.data.db.entity.SubwayEntity

@Dao
interface StationDao {

    @Transaction    //쿼리일때만 사용함
    @Query("SELECT * FROM StationEntity")
    //Flow -> 옵저버블 데이터 타입 (라이브 데이터와 유사)
    fun getStationWithSubways(): Flow<List<StationWithSubwaysEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(station: List<StationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubways(subways: List<SubwayEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossReferences(reference: List<StationSubwayCrossRefEntity>)

    @Transaction
    suspend fun insertStationSubways(stationSubways: List<Pair<StationEntity, SubwayEntity>>) {
        insertStations(stationSubways.map { it.first })
        insertSubways(stationSubways.map { it.second })
        insertCrossReferences(
            stationSubways.map { (station, subway) ->
                StationSubwayCrossRefEntity(
                    station.stationName,
                    subway.subwayId
                )
            }
        )
    }

    @Update
    suspend fun updateStation(station: StationEntity)
}