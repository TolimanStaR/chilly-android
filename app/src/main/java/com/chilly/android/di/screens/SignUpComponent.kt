package com.chilly.android.di.screens

import androidx.compose.material3.SnackbarHostState
import com.chilly.android.di.application.ApplicationComponent
import com.chilly.android.presentation.screens.sign_up.SignUpNewsCollector
import com.chilly.android.presentation.screens.sign_up.SignUpStore
import dagger.Component
import javax.inject.Scope

@SignUpScope
@Component(dependencies = [ApplicationComponent::class])
interface SignUpComponent {

    fun store(): SignUpStore
    val newsCollector: SignUpNewsCollector

    @Component.Builder
    interface Builder {
        fun appComponent(component: ApplicationComponent): Builder
        fun build(): SignUpComponent
    }
}

@Scope
annotation class SignUpScope