package com.chilly.android.di.screens


import com.chilly.android.di.application.ApplicationComponent
import com.chilly.android.presentation.screens.history.HistoryNewsCollector
import com.chilly.android.presentation.screens.history.HistoryStore
import com.chilly.android.presentation.screens.history.HistoryUiMapper
import dagger.Component
import javax.inject.Scope

@HistoryScope
@Component(dependencies = [ApplicationComponent::class])
interface HistoryComponent {

    fun store(): HistoryStore
    val newsCollector: HistoryNewsCollector
    val uiStateMapper: HistoryUiMapper

    @Component.Builder
    interface Builder {
        fun appComponent(applicationComponent: ApplicationComponent): Builder
        fun build(): HistoryComponent
    }
}

@Scope
annotation class HistoryScope