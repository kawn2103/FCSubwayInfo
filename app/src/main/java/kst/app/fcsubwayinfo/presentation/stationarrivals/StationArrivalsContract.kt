package kst.app.fcsubwayinfo.presentation.stationarrivals

import kst.app.fcsubwayinfo.domain.ArrivalInformation
import kst.app.fcsubwayinfo.presentation.BasePresenter
import kst.app.fcsubwayinfo.presentation.BaseView

interface StationArrivalsContract {

    interface View : BaseView<Presenter> {

        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showErrorDescription(message: String)

        fun showStationArrivals(arrivalInformation: List<ArrivalInformation>)
    }

    interface Presenter : BasePresenter {

        fun fetchStationArrivals()

        fun toggleStationFavorite()
    }
}