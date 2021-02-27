package com.example.composepegel

import android.app.Application
import com.example.composepegel.network.Client
import com.example.composepegel.network.ClientImpl
import com.example.composepegel.network.HTTPRepository
import com.example.composepegel.network.HTTPRepositoryImpl
import com.example.composepegel.ui.station.StationViewModel
import com.example.composepegel.ui.water.WaterViewModel
import com.example.composepegel.ui.waters.WatersViewModel
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        AndroidThreeTen.init(this)
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }

    private val appModule = module {
        single<Client> { ClientImpl() }
        single<HTTPRepository> { HTTPRepositoryImpl(get()) }

        viewModel { (stationUuid: String) -> StationViewModel(get(), stationUuid) }
        viewModel { (waterShortName: String) -> WaterViewModel(get(), waterShortName) }
        viewModel { WatersViewModel(get()) }
    }
}