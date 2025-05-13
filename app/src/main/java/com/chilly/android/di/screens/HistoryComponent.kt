package com.chilly.android.di.screens


import com.chilly.android.di.activity.ActivityComponent
import com.chilly.android.presentation.screens.history.HistoryNewsCollector
import com.chilly.android.presentation.screens.history.HistoryStore
import com.chilly.android.presentation.screens.history.HistoryUiMapper
import dagger.Component
import javax.inject.Scope

@HistoryScope
@Component(dependencies = [ActivityComponent::class])
interface HistoryComponent {

    fun store(): HistoryStore
    val newsCollector: HistoryNewsCollector
    val uiStateMapper: HistoryUiMapper

    @Component.Builder
    interface Builder {
        fun appComponent(activityComponent: ActivityComponent): Builder
        fun build(): HistoryComponent
    }
}

@Scope
annotation class HistoryScope