package kst.app.fcsubwayinfo.data.db.entity.mapper

import kst.app.fcsubwayinfo.data.db.entity.StationWithSubwaysEntity
import kst.app.fcsubwayinfo.data.db.entity.SubwayEntity
import kst.app.fcsubwayinfo.domain.Station
import kst.app.fcsubwayinfo.domain.Subway

fun StationWithSubwaysEntity.toStation() = Station(
    name = station.stationName,
    isFavorited = station.isFavorited,
    connectedSubways = subways.toSubways()
)

fun List<StationWithSubwaysEntity>.toStations() = map { it.toStation() }

fun List<SubwayEntity>.toSubways(): List<Subway> = map { Subway.findById(it.subwayId) }