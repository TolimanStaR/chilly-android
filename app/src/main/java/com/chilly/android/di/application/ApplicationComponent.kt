package com.chilly.android.di.application

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import com.chilly.android.data.remote.TokenHolder
import com.chilly.android.data.remote.api.LoginApi
import com.chilly.android.data.remote.api.PasswordRecoveryApi
import com.chilly.android.domain.repository.PreferencesRepository
import com.chilly.android.presentation.common.structure.ResourcesHolder
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, NetworkModule::class, NavigationModule::class])
interface ApplicationComponent {

    val preferencesRepository: PreferencesRepository
    val loginApi: LoginApi
    val recoveryApi: PasswordRecoveryApi

    val navigatorHolder: NavigatorHolder
    val router: Router
    val snackbarHostState: SnackbarHostState

    val tokenHolder: TokenHolder
    val resourceHolder: ResourcesHolder

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(context: Context): Builder
        fun build(): ApplicationComponent
    }
}