package com.chilly.android.di.screens


import com.chilly.android.di.application.ApplicationComponent
import com.chilly.android.presentation.screens.quiz.QuizNewsCollector
import com.chilly.android.presentation.screens.quiz.QuizStore
import dagger.Component
import javax.inject.Scope

@QuizScope
@Component(dependencies = [ApplicationComponent::class])
interface QuizComponent {

    fun store(): QuizStore
    val newsCollector: QuizNewsCollector

    @Component.Builder
    interface Builder {
        fun appComponent(applicationComponent: ApplicationComponent): Builder
        fun build(): QuizComponent
    }
}

@Scope
annotation class QuizScope