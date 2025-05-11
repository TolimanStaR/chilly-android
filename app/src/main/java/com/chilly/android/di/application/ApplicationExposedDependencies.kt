package com.chilly.android.di.application

import com.chilly.android.data.local.dao.HistoryDao
import com.chilly.android.data.local.dao.PlaceDao
import com.chilly.android.data.remote.TokenHolder
import com.chilly.android.data.remote.api.CommentsApi
import com.chilly.android.data.remote.api.FeedApi
import com.chilly.android.data.remote.api.LoginApi
import com.chilly.android.data.remote.api.PasswordRecoveryApi
import com.chilly.android.data.remote.api.QuizApi
import com.chilly.android.data.remote.api.RecommendationApi
import com.chilly.android.data.remote.api.UserApi
import com.chilly.android.domain.repository.CommentsRepository
import com.chilly.android.domain.repository.FeedRepository
import com.chilly.android.domain.repository.PlaceRepository
import com.chilly.android.domain.repository.PreferencesRepository
import com.chilly.android.domain.repository.QuizRepository
import com.chilly.android.domain.repository.RecommendationRepository
import com.chilly.android.domain.repository.UserRepository
import com.chilly.android.presentation.common.structure.WorkManagerProvider
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router

interface ApplicationExposedDependencies :
        ApiDependencies,
        DaoDependencies,
        RepositoryDependencies,
        NavigationDependencies,
        ServiceDependencies

interface ApiDependencies {
    val loginApi: LoginApi
    val recoveryApi: PasswordRecoveryApi
    val userApi: UserApi
    val quizApi: QuizApi
    val recommendationApi: RecommendationApi
    val feedApi: FeedApi
    val commentsApi: CommentsApi
}

interface DaoDependencies {
    val placeDao: PlaceDao
    val historyDao: HistoryDao
}

interface RepositoryDependencies {
    val preferencesRepository: PreferencesRepository
    val userRepository: UserRepository
    val quizRepository: QuizRepository
    val placeRepository: PlaceRepository
    val recommendationRepository: RecommendationRepository
    val feedRepository: FeedRepository
    val commentsRepository: CommentsRepository
}

interface NavigationDependencies {
    val navigatorHolder: NavigatorHolder
    val router: Router
}

interface ServiceDependencies {
    val workManagerProvider: WorkManagerProvider
    val tokenHolder: TokenHolder
}