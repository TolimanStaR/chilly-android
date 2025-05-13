package com.chilly.android.di.activity

import androidx.compose.material3.SnackbarHostState
import dagger.Module
import dagger.Provides

@Module
class ActivityModule {
    @ActivityScope
    @Provides
    fun provideSnackbarHostState(): SnackbarHostState = SnackbarHostState()
}