package com.chilly.android.di.screens

import com.chilly.android.di.activity.ActivityComponent
import com.chilly.android.presentation.screens.sign_up.SignUpNewsCollector
import com.chilly.android.presentation.screens.sign_up.SignUpStore
import dagger.Component
import javax.inject.Scope

@SignUpScope
@Component(dependencies = [ActivityComponent::class])
interface SignUpComponent {

    fun store(): SignUpStore
    val newsCollector: SignUpNewsCollector

    @Component.Builder
    interface Builder {
        fun appComponent(activityComponent: ActivityComponent): Builder
        fun build(): SignUpComponent
    }
}

@Scope
annotation class SignUpScope