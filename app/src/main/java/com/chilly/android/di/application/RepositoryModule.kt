package com.chilly.android.di.application

import android.content.Context
import com.chilly.android.data.remote.api.QuizApi
import com.chilly.android.data.remote.api.UserApi
import com.chilly.android.data.repository.PlaceRepositoryImpl
import com.chilly.android.data.repository.PreferencesRepositoryImpl
import com.chilly.android.data.repository.QuizRepositoryImpl
import com.chilly.android.data.repository.UserRepositoryImpl
import com.chilly.android.domain.repository.PlaceRepository
import com.chilly.android.domain.repository.PreferencesRepository
import com.chilly.android.domain.repository.QuizRepository
import com.chilly.android.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun providePreferencesRepository(
        applicationContext: Context
    ): PreferencesRepository {
        return PreferencesRepositoryImpl(applicationContext)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        userApi: UserApi
    ): UserRepository = UserRepositoryImpl(userApi)


    @Provides
    @Singleton
    fun provideQuizRepository(
        quizApi: QuizApi
    ): QuizRepository = QuizRepositoryImpl(quizApi)

    @Provides
    @Singleton
    fun providePlaceRepository(): PlaceRepository = PlaceRepositoryImpl()

}