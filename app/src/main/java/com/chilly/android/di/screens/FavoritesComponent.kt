package com.chilly.android.di.screens


import com.chilly.android.di.activity.ActivityComponent
import com.chilly.android.presentation.screens.favorites.FavoritesNewsCollector
import com.chilly.android.presentation.screens.favorites.FavoritesStore
import dagger.Component
import javax.inject.Scope

@FavoritesScope
@Component(dependencies = [ActivityComponent::class])
interface FavoritesComponent {

    fun store(): FavoritesStore
    val newsCollector: FavoritesNewsCollector

    @Component.Builder
    interface Builder {
        fun appComponent(activityComponent: ActivityComponent): Builder
        fun build(): FavoritesComponent
    }
}

@Scope
annotation class FavoritesScope