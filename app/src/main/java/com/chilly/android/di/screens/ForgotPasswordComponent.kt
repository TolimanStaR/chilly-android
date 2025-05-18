package com.chilly.android.di.screens

import com.chilly.android.di.activity.ActivityComponent
import com.chilly.android.presentation.screens.forgotPassword.ForgotPasswordNewsCollector
import com.chilly.android.presentation.screens.forgotPassword.ForgotPasswordStore
import dagger.Component
import javax.inject.Scope

@ForgotPasswordScope
@Component(dependencies = [ActivityComponent::class])
interface ForgotPasswordComponent {

    fun store(): ForgotPasswordStore
    val newsCollector: ForgotPasswordNewsCollector

    @Component.Builder
    interface Builder {
        fun appComponent(activityComponent: ActivityComponent): Builder
        fun build(): ForgotPasswordComponent
    }
}

@Scope
annotation class ForgotPasswordScope