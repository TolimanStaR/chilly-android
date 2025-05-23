package com.chilly.android.di.screens

import com.chilly.android.di.activity.ActivityComponent
import com.chilly.android.presentation.screens.splash.SplashScreenViewModel
import dagger.Component
import javax.inject.Scope


@SplashScope
@Component(dependencies = [ActivityComponent::class])
interface SplashScreenComponent {

    fun viewModel(): SplashScreenViewModel

    @Component.Builder
    interface Builder {
        fun appComponent(activityComponent: ActivityComponent): Builder
        fun build(): SplashScreenComponent
    }
}

@Scope
annotation class SplashScope