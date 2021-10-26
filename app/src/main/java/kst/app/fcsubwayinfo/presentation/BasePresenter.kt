package kst.app.fcsubwayinfo.presentation

import androidx.annotation.CallSuper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

interface BasePresenter {
    //앱에서 사용되는 Presenter Base 선언

    //코루틴 스코프 선언
    val scope: CoroutineScope
        get()  = MainScope()

    //뷰 생성 선언
    fun onViewCreated()
    //뷰 삭제 선언
    fun onDestroyView()
    //파괴 선언
    @CallSuper
    fun onDestroy(){
        scope.cancel()
    }
}