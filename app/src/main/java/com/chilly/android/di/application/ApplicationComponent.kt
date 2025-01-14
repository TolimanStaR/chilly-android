package com.chilly.android.di.application

import android.content.Context
import com.chilly.android.di.repository.RepositoryModule
import com.chilly.android.presentation.onboarding.OnBoardingViewModel
import com.chilly.android.presentation.splash.SplashScreenViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class])
interface ApplicationComponent {

    fun splashScreenViewModelFactory(): SplashScreenViewModel.Factory
    fun onBoardingViewModelFactory(): OnBoardingViewModel.Factory

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(context: Context): Builder

        fun build(): ApplicationComponent
    }
}