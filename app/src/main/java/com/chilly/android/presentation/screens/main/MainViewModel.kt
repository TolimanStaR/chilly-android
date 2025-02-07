package com.chilly.android.presentation.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chilly.android.domain.repository.PreferencesRepository
import com.chilly.android.presentation.navigation.Destination
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class MainViewModel @AssistedInject constructor(
    private val prefs: PreferencesRepository,
    @Assisted private val onNavigate: (Destination) -> Unit,
) : ViewModel() {

    fun onExitClicked() {
        viewModelScope.launch {
            prefs.saveRefreshToken("")
            onNavigate.invoke(Destination.LogIn)
        }
    }

    fun onShowOnboarding() {
        viewModelScope.launch {
            prefs.setHasSeenOnboarding(false)
            onNavigate.invoke(Destination.Onboarding(0))
        }
    }

    @AssistedFactory
    interface Factory {
        fun build(onNavigate: (Destination) -> Unit): MainViewModel
    }

}