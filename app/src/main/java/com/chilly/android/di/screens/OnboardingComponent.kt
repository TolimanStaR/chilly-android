package com.chilly.android.di.screens

import com.chilly.android.di.activity.ActivityComponent
import com.chilly.android.presentation.screens.onboarding.OnboardingEffectCollector
import com.chilly.android.presentation.screens.onboarding.OnboardingViewModel
import dagger.Component
import javax.inject.Scope

@OnboardingScope
@Component(dependencies = [ActivityComponent::class])
interface OnboardingComponent {

    fun viewModel(): OnboardingViewModel
    val effectCollector: OnboardingEffectCollector

    @Component.Builder
    interface Builder {
        fun appComponent(activityComponent: ActivityComponent): Builder
        fun build(): OnboardingComponent
    }
}

@Scope
annotation class OnboardingScope