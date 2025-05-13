package com.chilly.android.di.screens

import com.chilly.android.di.activity.ActivityComponent
import com.chilly.android.presentation.screens.main.MainNewsCollector
import com.chilly.android.presentation.screens.main.MainStore
import dagger.Component
import javax.inject.Scope

@MainScope
@Component(dependencies = [ActivityComponent::class])
interface MainComponent {

    fun store(): MainStore
    val newsCollector: MainNewsCollector

    @Component.Builder
    interface Builder {
        fun appComponent(activityComponent: ActivityComponent): Builder
        fun build(): MainComponent
    }
}

@Scope
annotation class MainScope