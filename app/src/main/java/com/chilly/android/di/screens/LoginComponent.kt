package com.chilly.android.di.screens

import androidx.compose.material3.SnackbarHostState
import com.chilly.android.di.application.ApplicationComponent
import com.chilly.android.presentation.screens.login.LoginNewsCollector
import com.chilly.android.presentation.screens.login.LoginStore
import dagger.Component
import javax.inject.Scope

@LoginScope
@Component(dependencies = [ApplicationComponent::class])
interface LoginComponent {

    val snackbarHostState: SnackbarHostState

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