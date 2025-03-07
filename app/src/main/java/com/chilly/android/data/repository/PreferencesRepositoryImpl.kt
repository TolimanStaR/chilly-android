package com.chilly.android.data.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.chilly.android.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.first
import timber.log.Timber

private val SEEN_ONBOARDING_KEY = booleanPreferencesKey("SEEN_ONBOARDING")
private val REFRESH_TOKEN_KEY = stringPreferencesKey("REFRESH_TOKEN")
private val COMPLETED_MAIN_QUIZ = booleanPreferencesKey("COMPLETED_MAIN_QUIZ")
private val REQUESTED_RECOMMENDATION = booleanPreferencesKey("REQUESTED_RECOMMENDATION")

class PreferencesRepositoryImpl(
    private val context: Context,
) : PreferencesRepository {

    private val Context.dataStore by preferencesDataStore(name = "settings")

    override suspend fun hasSeenOnboarding(): Boolean {
        return getPrefs()[SEEN_ONBOARDING_KEY] ?: false
    }

    override suspend fun setHasSeenOnboarding(value: Boolean) {
        updatePref(SEEN_ONBOARDING_KEY, value)
    }

    override suspend fun getSavedRefreshToken(): String? {
        Timber.e("searching for token in prefs")
        return getPrefs()[REFRESH_TOKEN_KEY]
            .also {
                Timber.e("got token: $it")
            }
    }

    override suspend fun saveRefreshToken(token: String?) {
        if (token == null) {
            removePref(REFRESH_TOKEN_KEY)
        } else {
            updatePref(REFRESH_TOKEN_KEY, token)
        }
    }

    override suspend fun hasCompletedMainQuiz(): Boolean {
        return  (getPrefs()[COMPLETED_MAIN_QUIZ] ?: false)
            .also {
                Timber.e("retrieving quiz completion: $it")
            }
    }

    override suspend fun setHasCompletedMainQuiz(value: Boolean) {
        Timber.e("setting quiz completion: $value")
        updatePref(COMPLETED_MAIN_QUIZ, value)
    }

    override suspend fun hasRequestedRecommendation(): Boolean {
        return getPrefs()[REQUESTED_RECOMMENDATION] ?: false
    }

    override suspend fun setRequestedRecommendation(value: Boolean) {
        updatePref(REQUESTED_RECOMMENDATION, value)
    }

    private suspend fun getPrefs(): Preferences =
        context.dataStore.data.first()

    private suspend fun <T> updatePref(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { prefs ->
            prefs[key] = value
        }
    }

    private suspend fun removePref(key: Preferences.Key<*>) {
        context.dataStore.edit { prefs ->
            prefs.remove(key)
        }
    }

}