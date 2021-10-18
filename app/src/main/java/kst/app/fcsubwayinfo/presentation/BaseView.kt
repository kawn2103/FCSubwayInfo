package kst.app.fcsubwayinfo.presentation

interface BaseView<PresenterT: BasePresenter> {

    val presenter:PresenterT
}