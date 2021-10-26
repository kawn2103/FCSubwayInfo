package kst.app.fcsubwayinfo.presentation

interface BaseView<PresenterT: BasePresenter> {
    //앱에서 사용되는 View Base 선언
    val presenter:PresenterT
}