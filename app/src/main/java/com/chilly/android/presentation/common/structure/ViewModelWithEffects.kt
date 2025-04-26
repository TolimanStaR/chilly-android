package com.chilly.android.presentation.common.structure

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

abstract class ViewModelWithEffects<Effect: Any, Event: Any>(
    @VisibleForTesting effectsReplay: Int = 0
) : ViewModel() {

    private val _effects = MutableSharedFlow<Effect>(replay = effectsReplay)
    val effects: Flow<Effect> = _effects.asSharedFlow()

    protected fun emit(effect: Effect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }

    abstract fun dispatch(event: Event)
}