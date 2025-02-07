package com.chilly.android.di.screens

import com.chilly.android.di.application.ApplicationComponent
import com.chilly.android.presentation.screens.splash.SplashScreenEffectCollector
import com.chilly.android.presentation.screens.splash.SplashScreenViewModel
import dagger.Component
import javax.inject.Scope


@SplashScope
@Component(dependencies = [ApplicationComponent::class])
interface SplashScreenComponent {

    fun viewModel(): SplashScreenViewModel
    val effectCollector: SplashScreenEffectCollector

    @Component.Builder
    interface Builder {
        fun appComponent(applicationComponent: ApplicationComponent): Builder
        fun build(): SplashScreenComponent
    }
}

@Scope
annotation class SplashScope