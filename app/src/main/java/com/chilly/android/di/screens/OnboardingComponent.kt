package com.chilly.android.di.screens

import com.chilly.android.di.application.ApplicationComponent
import com.chilly.android.presentation.onboarding.OnBoardingViewModel
import dagger.Component
import javax.inject.Scope

@OnboardingScope
@Component(dependencies = [ApplicationComponent::class])
interface OnboardingComponent {

    fun viewModelFactory(): OnBoardingViewModel.Factory

    @Component.Builder
    interface Builder {
        fun appComponent(appComponent: ApplicationComponent): Builder
        fun build(): OnboardingComponent
    }
}

@Scope
annotation class OnboardingScope