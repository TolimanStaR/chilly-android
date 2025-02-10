package com.chilly.android.di.screens

import com.chilly.android.di.application.ApplicationComponent
import com.chilly.android.presentation.screens.forgot_password.ForgotPasswordNewsCollector
import com.chilly.android.presentation.screens.forgot_password.ForgotPasswordStore
import dagger.Component
import javax.inject.Scope

@ForgotPasswordScope
@Component(dependencies = [ApplicationComponent::class])
interface ForgotPasswordComponent {

    fun store(): ForgotPasswordStore
    val newsCollector: ForgotPasswordNewsCollector

    @Component.Builder
    interface Builder {
        fun appComponent(applicationComponent: ApplicationComponent): Builder
        fun build(): ForgotPasswordComponent
    }
}

@Scope
annotation class ForgotPasswordScope