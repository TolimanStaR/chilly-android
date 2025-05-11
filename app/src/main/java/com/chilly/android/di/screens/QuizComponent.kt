package com.chilly.android.di.screens


import com.chilly.android.di.activity.ActivityComponent
import com.chilly.android.presentation.screens.quiz.QuizNewsCollector
import com.chilly.android.presentation.screens.quiz.QuizStore
import dagger.Component
import javax.inject.Scope

@QuizScope
@Component(dependencies = [ActivityComponent::class])
interface QuizComponent {

    fun store(): QuizStore
    val newsCollector: QuizNewsCollector

    @Component.Builder
    interface Builder {
        fun appComponent(activityComponent: ActivityComponent): Builder
        fun build(): QuizComponent
    }
}

@Scope
annotation class QuizScope