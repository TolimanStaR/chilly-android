package com.chilly.android.di.screens

import com.chilly.android.di.application.ApplicationComponent
import com.chilly.android.presentation.onboarding.OnboardingViewModel
import dagger.Component
import javax.inject.Scope

@OnboardingScope
@Component(dependencies = [ApplicationComponent::class])
interface OnboardingComponent {

    fun viewModelFactory(): OnboardingViewModel.Factory

    @Component.Builder
    interface Builder {
        fun appComponent(appComponent: ApplicationComponent): Builder
        fun build(): OnboardingComponent
    }
}

@Scope
annotation class OnboardingScope