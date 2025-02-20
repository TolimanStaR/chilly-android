package com.chilly.android.data.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.chilly.android.domain.repository.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

private val SEEN_ONBOARDING_KEY = booleanPreferencesKey("SEEN_ONBOARDING")
private val REFRESH_TOKEN_KEY = stringPreferencesKey("REFRESH_TOKEN")
private val COMPLETED_MAIN_QUIZ = booleanPreferencesKey("COMPLETED_MAIN_QUIZ")

class PreferencesRepositoryImpl(
    private val context: Context,
) : PreferencesRepository {

    private val Context.dataStore by preferencesDataStore(name = "settings")
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val data = context.dataStore.data
        .stateIn(scope, SharingStarted.Eagerly, emptyPreferences())

    override suspend fun hasSeenOnboarding(): Boolean {
        return data.value[SEEN_ONBOARDING_KEY] ?: false
    }

    override suspend fun setHasSeenOnboarding(value: Boolean) {
        updatePref(SEEN_ONBOARDING_KEY, value)
    }

    override suspend fun getSavedRefreshToken(): String? {
        Timber.e("searching for token in prefs")
        return data.value[REFRESH_TOKEN_KEY]
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
        return  (data.value[COMPLETED_MAIN_QUIZ] ?: false)
            .also {
                Timber.e("retrieving quiz completion: $it")
            }
    }

    override suspend fun setHasCompletedMainQuiz(value: Boolean) {
        Timber.e("setting quiz completion: $value")
        updatePref(COMPLETED_MAIN_QUIZ, value)
    }

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