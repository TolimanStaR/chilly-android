package com.chilly.android.di.screens


import com.chilly.android.di.activity.ActivityComponent
import com.chilly.android.presentation.screens.result.RecommendationResultNewsCollector
import com.chilly.android.presentation.screens.result.RecommendationResultStore
import dagger.Component
import javax.inject.Scope

@RecommendationResultScope
@Component(dependencies = [ActivityComponent::class])
interface RecommendationResultComponent {

    fun store(): RecommendationResultStore
    val newsCollector: RecommendationResultNewsCollector

    @Component.Builder
    interface Builder {
        fun appComponent(activityComponent: ActivityComponent): Builder
        fun build(): RecommendationResultComponent
    }
}

@Scope
annotation class RecommendationResultScope