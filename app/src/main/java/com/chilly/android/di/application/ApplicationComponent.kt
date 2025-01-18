package com.chilly.android.di.application

import android.content.Context
import com.chilly.android.data.remote.api.LoginApi
import com.chilly.android.domain.repository.PreferencesRepository
import com.chilly.android.presentation.main.MainViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, NetworkModule::class])
interface ApplicationComponent {

    fun preferencesRepository(): PreferencesRepository
    fun loginApi(): LoginApi

    fun mainViewModelFactory(): MainViewModel.Factory

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(context: Context): Builder

        fun build(): ApplicationComponent
    }
}