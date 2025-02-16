package com.chilly.android.di.screens

import com.chilly.android.di.application.ApplicationComponent
import com.chilly.android.presentation.screens.main.MainNewsCollector
import com.chilly.android.presentation.screens.main.MainStore
import dagger.Component
import javax.inject.Scope

@MainScope
@Component(dependencies = [ApplicationComponent::class])
interface MainComponent {

    fun store(): MainStore
    val newsCollector: MainNewsCollector

    @Component.Builder
    interface Builder {
        fun appComponent(applicationComponent: ApplicationComponent): Builder
        fun build(): MainComponent
    }
}

@Scope
annotation class MainScope