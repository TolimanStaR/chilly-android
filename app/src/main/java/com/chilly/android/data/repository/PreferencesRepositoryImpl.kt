package com.chilly.android.data.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.chilly.android.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.firstOrNull

private val SEEN_ONBOARDING_KEY = booleanPreferencesKey("SEEN_ONBOARDING")
private val REFRESH_TOKEN_KEY = stringPreferencesKey("REFRESH_TOKEN")

class PreferencesRepositoryImpl(
    private val context: Context,
) : PreferencesRepository {

    private val Context.dataStore by preferencesDataStore(name = "settings")

    override suspend fun hasSeenOnboarding(): Boolean {
        val prefs = getPrefs()
        if (prefs == null) {
            setHasSeenOnboarding(false)
            return false
        }
        return prefs[SEEN_ONBOARDING_KEY] ?: false
    }

    override suspend fun setHasSeenOnboarding(value: Boolean) {
        updatePref(SEEN_ONBOARDING_KEY, value)
    }

    override suspend fun getSavedRefreshToken(): String? {
        val prefs = getPrefs() ?: return null
        return prefs[REFRESH_TOKEN_KEY]
    }

    override suspend fun saveRefreshToken(token: String) {
        updatePref(REFRESH_TOKEN_KEY, token)
    }

    private suspend fun getPrefs(): Preferences?
        = context.dataStore.data.firstOrNull()

    private suspend fun <T> updatePref(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { prefs ->
            prefs[key] = value
        }
    }

}