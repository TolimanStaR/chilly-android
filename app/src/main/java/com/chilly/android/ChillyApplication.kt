package com.chilly.android

import android.app.Application
import android.content.Context
import com.chilly.android.di.application.ApplicationComponent
import com.chilly.android.di.application.DaggerApplicationComponent
import timber.log.Timber

class ChillyApplication : Application() {

    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerApplicationComponent.builder()
            .application(this)
            .build()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}

val Context.applicationComponent: ApplicationComponent
    get() = (applicationContext as ChillyApplication).component