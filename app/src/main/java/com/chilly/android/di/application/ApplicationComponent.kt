package com.chilly.android.di.application

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, NetworkModule::class, NavigationModule::class, DatabaseModule::class])
interface ApplicationComponent : ApplicationExposedDependencies {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(context: Context): Builder
        fun build(): ApplicationComponent
    }
}