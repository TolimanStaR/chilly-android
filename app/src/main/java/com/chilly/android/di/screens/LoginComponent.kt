package com.chilly.android.di.screens

import com.chilly.android.di.activity.ActivityComponent
import com.chilly.android.presentation.screens.login.LoginNewsCollector
import com.chilly.android.presentation.screens.login.LoginStore
import dagger.Component
import javax.inject.Scope

@LoginScope
@Component(dependencies = [ActivityComponent::class])
interface LoginComponent {

    fun store(): LoginStore
    val newsCollector: LoginNewsCollector

    @Component.Builder
    interface Builder {
        fun appComponent(activityComponent: ActivityComponent): Builder
        fun build(): LoginComponent
    }
}

@Scope
annotation class LoginScope