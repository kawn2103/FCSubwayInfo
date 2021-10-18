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

    single { Dispatchers.IO }

    // Database
    single { AppDatabase.build(androidApplication()) }
    single { get<AppDatabase>().stationDao() }

    // Preference
    single { androidContext().getSharedPreferences("preference", Activity.MODE_PRIVATE) }
    single<PreferenceManager> { SharedPreferenceManager(get()) }


    // Api
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

    // Repository
    single<StationRepository> { StationRepositoryImpl(get(), get(), get(), get(),get()) }

    // Presentation
    scope<StationsFragment> {
        scoped<StationsContract.Presenter> { StationsPresenter(getSource(), get()) }
    }
}