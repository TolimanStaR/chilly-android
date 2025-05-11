package com.chilly.android.di.screens


import com.chilly.android.di.activity.ActivityComponent
import com.chilly.android.presentation.screens.profile.ProfileNewsCollector
import com.chilly.android.presentation.screens.profile.ProfileStore
import dagger.Component
import javax.inject.Scope

@ProfileScope
@Component(dependencies = [ActivityComponent::class])
interface ProfileComponent {

    fun store(): ProfileStore
    val newsCollector: ProfileNewsCollector

    @Component.Builder
    interface Builder {
        fun appComponent(activityComponent: ActivityComponent): Builder
        fun build(): ProfileComponent
    }
}

@Scope
annotation class ProfileScope