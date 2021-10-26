package kst.app.fcsubwayinfo

import android.app.Application
import kst.app.fcsubwayinfo.di.appModule
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class SubwayApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        //앱 시작 시 Koin 시작
        startKoin {
            //로그를 남기기 위한 코드 첨가
            androidLogger(
                if (BuildConfig.DEBUG){
                    Level.DEBUG
                }else {
                    Level.NONE
                }
            )
            //앱의 Context 첨가
            androidContext(this@SubwayApplication)
            //앱 모듈 주입
            modules(appModule)
        }
    }

}