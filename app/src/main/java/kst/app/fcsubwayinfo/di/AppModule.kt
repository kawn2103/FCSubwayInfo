package kst.app.fcsubwayinfo.di

import android.app.Activity
import com.google.firebase.ktx.BuildConfig
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kst.app.fcsubwayinfo.data.api.StationApi
import kst.app.fcsubwayinfo.data.api.StationArrivalsApi
import kst.app.fcsubwayinfo.data.api.StationStorageApi
import kst.app.fcsubwayinfo.data.api.Url
import kst.app.fcsubwayinfo.data.db.AppDatabase
import kst.app.fcsubwayinfo.data.preference.PreferenceManager
import kst.app.fcsubwayinfo.data.preference.SharedPreferenceManager
import kst.app.fcsubwayinfo.data.repository.StationRepository
import kst.app.fcsubwayinfo.data.repository.StationRepositoryImpl
import kst.app.fcsubwayinfo.presentation.stationarrivals.StationArrivalsContract
import kst.app.fcsubwayinfo.presentation.stationarrivals.StationArrivalsFragment
import kst.app.fcsubwayinfo.presentation.stationarrivals.StationArrivalsPresenter
import kst.app.fcsubwayinfo.presentation.stations.StationsContract
import kst.app.fcsubwayinfo.presentation.stations.StationsFragment
import kst.app.fcsubwayinfo.presentation.stations.StationsPresenter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

val appModule = module {

    // 코루틴 스코트 싱글턴 생성
    single { Dispatchers.IO }

    // Database 싱글턴 생성
    single { AppDatabase.build(androidApplication()) }
    single { get<AppDatabase>().stationDao() }

    // Preference 싱글턴 생성
    single { androidContext().getSharedPreferences("preference", Activity.MODE_PRIVATE) }
    single<PreferenceManager> { SharedPreferenceManager(get()) }


    // Api 싱글턴 생성
    single {
        OkHttpClient()
                .newBuilder()
                .addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = if (BuildConfig.DEBUG) {
                                HttpLoggingInterceptor.Level.BODY
                            } else {
                                HttpLoggingInterceptor.Level.NONE
                            }
                        }
                )
                .build()
    }

    single<StationArrivalsApi> {
        Retrofit.Builder().baseUrl(Url.SEOUL_DATA_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(get())
                .build()
                .create()
    }


    single<StationApi> { StationStorageApi(Firebase.storage) }

    // Repository 싱글턴 생성
    single<StationRepository> { StationRepositoryImpl(get(), get(), get(), get(), get()) }

    // Presentation scope 생성
    /***********************************************************************************************
    getSource() -  프로젝스 소스코드에서 알맞은 객체를 가져와서 세팅
    get() - 모듈안에 선언되어 있는것 중 알맞은 객체를 가져와서 세팅함

    scope<적용될 scope 위치(fragment 혹은 Activity 등)>{
        scoped<implement로 받아오는 인터페이스의 타입> { 실제로 주입할 클래스(getSource(), get()) }
    }
    ***********************************************************************************************/
    scope<StationsFragment> {
        scoped<StationsContract.Presenter> { StationsPresenter(getSource(), get()) }
    }

    scope<StationArrivalsFragment> {
        scoped<StationArrivalsContract.Presenter> { StationArrivalsPresenter(getSource(), get(), get()) }
    }
}