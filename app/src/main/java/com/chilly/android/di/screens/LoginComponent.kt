package com.chilly.android.di.screens

import com.chilly.android.di.application.ApplicationComponent
import com.chilly.android.presentation.login.LoginNewsCollector
import com.chilly.android.presentation.login.LoginStore
import dagger.Component
import javax.inject.Scope

@LoginScope
@Component(dependencies = [ApplicationComponent::class])
interface LoginComponent {

    val appComponent: ApplicationComponent

    fun store(): LoginStore
    val newsCollector: LoginNewsCollector

    @Component.Builder
    interface Builder {
        fun appComponent(appComponent: ApplicationComponent): Builder
        fun build(): LoginComponent
    }
}

@Scope
annotation class LoginScope