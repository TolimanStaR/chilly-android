package com.chilly.android.di.application

import android.content.Context
import com.chilly.android.data.repository.PreferencesRepositoryImpl
import com.chilly.android.domain.repository.PreferencesRepository
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


}