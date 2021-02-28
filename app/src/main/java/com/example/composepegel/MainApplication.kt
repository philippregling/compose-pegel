package com.example.composepegel

import android.app.Application
import com.example.composepegel.database.DatabaseClient
import com.example.composepegel.database.DatabaseClientImpl
import com.example.composepegel.network.Client
import com.example.composepegel.network.ClientImpl
import com.example.composepegel.network.HTTPRepository
import com.example.composepegel.network.HTTPRepositoryImpl
import com.example.composepegel.ui.station.StationViewModel
import com.example.composepegel.ui.water.WaterViewModel
import com.example.composepegel.ui.waters.WatersViewModel
import com.example.composepegel.util.NetworkStateClient
import com.example.composepegel.util.NetworkStateClientImpl
import com.jakewharton.threetenabp.AndroidThreeTen
import io.realm.Realm
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber
import io.realm.RealmConfiguration


class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        AndroidThreeTen.init(this)
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .compactOnLaunch()
            .deleteRealmIfMigrationNeeded() // Migrations are not important in this app
            .build()
        Realm.setDefaultConfiguration(config)

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }

    private val appModule = module {
        single<Client> { ClientImpl(androidContext()) }
        single<HTTPRepository> { HTTPRepositoryImpl(get()) }
        single<DatabaseClient> { DatabaseClientImpl() }
        single<NetworkStateClient> { NetworkStateClientImpl() }

        viewModel { (stationUuid: String) -> StationViewModel(get(), stationUuid) }
        viewModel { (waterShortName: String) ->
            WaterViewModel(
                get(),
                get(),
                get(),
                waterShortName
            )
        }
        viewModel { WatersViewModel(get(), get(), get()) }
    }
}