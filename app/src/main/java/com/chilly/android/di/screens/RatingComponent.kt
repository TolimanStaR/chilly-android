package com.chilly.android.di.screens


import com.chilly.android.di.application.ApplicationComponent
import com.chilly.android.presentation.screens.rating.RatingNewsCollector
import com.chilly.android.presentation.screens.rating.RatingStore
import dagger.Component
import javax.inject.Scope

@RatingScope
@Component(dependencies = [ApplicationComponent::class])
interface RatingComponent {

    val storeFactory: RatingStore.Factory
    val newsCollector: RatingNewsCollector

    @Component.Builder
    interface Builder {
        fun appComponent(applicationComponent: ApplicationComponent): Builder
        fun build(): RatingComponent
    }
}

@Scope
annotation class RatingScope