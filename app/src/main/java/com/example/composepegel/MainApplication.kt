package com.example.composepegel

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import timber.log.Timber

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        AndroidThreeTen.init(this)
    }
}