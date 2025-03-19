package com.chilly.android.di.screens


import com.chilly.android.di.application.ApplicationComponent
import com.chilly.android.presentation.screens.favorites.FavoritesNewsCollector
import com.chilly.android.presentation.screens.favorites.FavoritesStore
import dagger.Component
import javax.inject.Scope

@FavoritesScope
@Component(dependencies = [ApplicationComponent::class])
interface FavoritesComponent {

    fun store(): FavoritesStore
    val newsCollector: FavoritesNewsCollector

    @Component.Builder
    interface Builder {
        fun appComponent(applicationComponent: ApplicationComponent): Builder
        fun build(): FavoritesComponent
    }
}

@Scope
annotation class FavoritesScope