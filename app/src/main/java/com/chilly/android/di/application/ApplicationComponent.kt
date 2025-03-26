package com.chilly.android.di.application

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import com.chilly.android.data.local.dao.HistoryDao
import com.chilly.android.data.local.dao.PlaceDao
import com.chilly.android.data.remote.TokenHolder
import com.chilly.android.data.remote.api.FeedApi
import com.chilly.android.data.remote.api.LoginApi
import com.chilly.android.data.remote.api.PasswordRecoveryApi
import com.chilly.android.data.remote.api.QuizApi
import com.chilly.android.data.remote.api.RecommendationApi
import com.chilly.android.data.remote.api.UserApi
import com.chilly.android.domain.repository.FeedRepository
import com.chilly.android.domain.repository.PlaceRepository
import com.chilly.android.domain.repository.PreferencesRepository
import com.chilly.android.domain.repository.QuizRepository
import com.chilly.android.domain.repository.RecommendationRepository
import com.chilly.android.domain.repository.UserRepository
import com.chilly.android.presentation.common.structure.ResourcesHolder
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, NetworkModule::class, NavigationModule::class, DatabaseModule::class])
interface ApplicationComponent {

    val preferencesRepository: PreferencesRepository
    val userRepository: UserRepository
    val quizRepository: QuizRepository
    val placeRepository: PlaceRepository
    val recommendationRepository: RecommendationRepository
    val feedRepository: FeedRepository

    val loginApi: LoginApi
    val recoveryApi: PasswordRecoveryApi
    val userApi: UserApi
    val quizApi: QuizApi
    val recommendationApi: RecommendationApi
    val feedApi: FeedApi

    val placeDao: PlaceDao
    val historyDao: HistoryDao

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