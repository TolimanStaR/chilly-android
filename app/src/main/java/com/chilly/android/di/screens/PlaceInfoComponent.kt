package com.chilly.android.di.screens


import com.chilly.android.di.application.ApplicationComponent
import com.chilly.android.presentation.screens.place.PlaceInfoNewsCollector
import com.chilly.android.presentation.screens.place.PlaceInfoStore
import dagger.BindsInstance
import dagger.Component
import javax.inject.Scope

@PlaceInfoScope
@Component(dependencies = [ApplicationComponent::class])
interface PlaceInfoComponent {

    val storeFactory: PlaceInfoStore.Factory
    val newsCollector: PlaceInfoNewsCollector

    @Component.Builder
    interface Builder {
        fun appComponent(applicationComponent: ApplicationComponent): Builder
        fun build(): PlaceInfoComponent
    }
}

@Scope
annotation class PlaceInfoScope