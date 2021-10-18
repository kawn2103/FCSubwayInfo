package kst.app.fcsubwayinfo.data.repository

import kotlinx.coroutines.flow.Flow
import kst.app.fcsubwayinfo.domain.Station

interface StationRepository {

    val stations: Flow<List<Station>>

    suspend fun refreshStations()
}