package com.chilly.android.di.application

import android.content.Context
import com.chilly.android.data.remote.api.LoginApi
import com.chilly.android.domain.repository.PreferencesRepository
import com.chilly.android.presentation.main.MainViewModel
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, NetworkModule::class, NavigationModule::class])
interface ApplicationComponent {

    fun preferencesRepository(): PreferencesRepository
    fun loginApi(): LoginApi
    fun navigatorHolder(): NavigatorHolder
    fun router(): Router

    fun mainViewModelFactory(): MainViewModel.Factory

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(context: Context): Builder

        fun build(): ApplicationComponent
    }
}