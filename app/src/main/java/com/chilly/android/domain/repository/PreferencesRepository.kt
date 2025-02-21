package com.chilly.android.domain.repository

interface PreferencesRepository {

    suspend fun hasSeenOnboarding(): Boolean

    suspend fun setHasSeenOnboarding(value: Boolean)

    suspend fun getSavedRefreshToken(): String?

    suspend fun saveRefreshToken(token: String?)

    suspend fun hasCompletedMainQuiz(): Boolean

    suspend fun setHasCompletedMainQuiz(value: Boolean)
}