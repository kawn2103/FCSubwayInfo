package kst.app.fcsubwayinfo.data.api

import kst.app.fcsubwayinfo.data.db.entity.StationEntity
import kst.app.fcsubwayinfo.data.db.entity.SubwayEntity

interface StationApi {

    suspend fun getStationDataUpdatedTimeMillis(): Long

    suspend fun getStationSubways(): List<Pair<StationEntity, SubwayEntity>>
}