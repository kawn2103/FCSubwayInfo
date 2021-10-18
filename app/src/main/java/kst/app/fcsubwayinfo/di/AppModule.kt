package kst.app.fcsubwayinfo.di

import android.app.Activity
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kst.app.fcsubwayinfo.data.api.StationApi
import kst.app.fcsubwayinfo.data.api.StationStorageApi
import kst.app.fcsubwayinfo.data.db.AppDatabase
import kst.app.fcsubwayinfo.data.preference.PreferenceManager
import kst.app.fcsubwayinfo.data.preference.SharedPreferenceManager
import kst.app.fcsubwayinfo.data.repository.StationRepository
import kst.app.fcsubwayinfo.data.repository.StationRepositoryImpl
import kst.app.fcsubwayinfo.presentation.stations.StationsContract
import kst.app.fcsubwayinfo.presentation.stations.StationsFragment
import kst.app.fcsubwayinfo.presentation.stations.StationsPresenter
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    single { Dispatchers.IO }

    // Database
    single { AppDatabase.build(androidApplication()) }
    single { get<AppDatabase>().stationDao() }

    // Preference
    single { androidContext().getSharedPreferences("preference", Activity.MODE_PRIVATE) }
    single<PreferenceManager> { SharedPreferenceManager(get()) }

    // Api
    single<StationApi> { StationStorageApi(Firebase.storage) }

    // Repository
    single<StationRepository> { StationRepositoryImpl(get(), get(), get(), get()) }

    // Presentation
    scope<StationsFragment> {
        scoped<StationsContract.Presenter> { StationsPresenter(getSource(), get()) }
    }
}