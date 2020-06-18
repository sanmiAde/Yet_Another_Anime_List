package com.sanmidev

import android.app.Application
import android.content.Context
import com.sanmidev.yetanotheranimelist.BuildConfig
import com.sanmidev.yetanotheranimelist.di.component.AppComponent
import com.sanmidev.yetanotheranimelist.di.component.DaggerAppComponent
import timber.log.Timber

class YetAnotherAnimeListApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

//    override fun attachBaseContext(base: Context?) {
//        super.attachBaseContext(base)
//        MultiDex.install(this)
//    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

    }
}


