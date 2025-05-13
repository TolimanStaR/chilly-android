package com.chilly.android.di.screens


import com.chilly.android.di.activity.ActivityComponent
import com.chilly.android.presentation.screens.place.PlaceInfoNewsCollector
import com.chilly.android.presentation.screens.place.PlaceInfoStore
import com.chilly.android.presentation.screens.place.PlaceUiMapper
import dagger.Component
import javax.inject.Scope

@PlaceInfoScope
@Component(dependencies = [ActivityComponent::class])
interface PlaceInfoComponent {

    val storeFactory: PlaceInfoStore.Factory
    val newsCollector: PlaceInfoNewsCollector
    val stateUiMapper: PlaceUiMapper

    @Component.Builder
    interface Builder {
        fun appComponent(activityComponent: ActivityComponent): Builder
        fun build(): PlaceInfoComponent
    }
}

@Scope
annotation class PlaceInfoScope