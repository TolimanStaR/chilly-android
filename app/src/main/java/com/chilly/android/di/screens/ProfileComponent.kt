package com.chilly.android.di.screens


import com.chilly.android.di.application.ApplicationComponent
import com.chilly.android.presentation.screens.profile.ProfileNewsCollector
import com.chilly.android.presentation.screens.profile.ProfileStore
import dagger.Component
import javax.inject.Scope

@ProfileScope
@Component(dependencies = [ApplicationComponent::class])
interface ProfileComponent {

    fun store(): ProfileStore
    val newsCollector: ProfileNewsCollector

    @Component.Builder
    interface Builder {
        fun appComponent(applicationComponent: ApplicationComponent): Builder
        fun build(): ProfileComponent
    }
}

@Scope
annotation class ProfileScope