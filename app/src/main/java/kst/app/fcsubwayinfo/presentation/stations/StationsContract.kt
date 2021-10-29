package kst.app.fcsubwayinfo.presentation.stations

import kst.app.fcsubwayinfo.domain.Station
import kst.app.fcsubwayinfo.presentation.BasePresenter
import kst.app.fcsubwayinfo.presentation.BaseView

// View와 Presenter의 Interface 모음
interface StationsContract {

    // BaseView를 상속받은 View Interface
    interface View : BaseView<Presenter> {

        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showStations(stations: List<Station>)
    }

    // BasePresenter를 상속받은 Presenter Interface
    interface Presenter : BasePresenter {
        fun filterStations(query: String)

        fun toggleStationFavorite(station: Station)
    }
}