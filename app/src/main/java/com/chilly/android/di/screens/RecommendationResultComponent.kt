package com.chilly.android.di.screens


import com.chilly.android.di.application.ApplicationComponent
import com.chilly.android.presentation.screens.result.RecommendationResultNewsCollector
import com.chilly.android.presentation.screens.result.RecommendationResultStore
import dagger.Component
import javax.inject.Scope

@RecommendationResultScope
@Component(dependencies = [ApplicationComponent::class])
interface RecommendationResultComponent {

    fun store(): RecommendationResultStore
    val newsCollector: RecommendationResultNewsCollector

    @Component.Builder
    interface Builder {
        fun appComponent(applicationComponent: ApplicationComponent): Builder
        fun build(): RecommendationResultComponent
    }
}

@Scope
annotation class RecommendationResultScope