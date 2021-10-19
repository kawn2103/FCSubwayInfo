package kst.app.fcsubwayinfo.presentation.stations

import kst.app.fcsubwayinfo.domain.Station
import kst.app.fcsubwayinfo.presentation.BasePresenter
import kst.app.fcsubwayinfo.presentation.BaseView

interface StationsContract {

    interface View : BaseView<Presenter> {

        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showStations(stations: List<Station>)
    }

    interface Presenter : BasePresenter {
        fun filterStations(query: String)

        fun toggleStationFavorite(station: Station)
    }
}