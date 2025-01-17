package com.chilly.android.presentation.common.structure

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner

class ViewModelFactory<VM: ViewModel>(
    savedStateRegistryOwner: SavedStateRegistryOwner,
    private val create: (stateHandle: SavedStateHandle) -> VM
) : AbstractSavedStateViewModelFactory(savedStateRegistryOwner, null) {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return create.invoke(handle) as T
    }

}

inline fun <reified VM : ViewModel> Context.lazyViewModel(
    noinline create: (stateHandle: SavedStateHandle) -> VM
): Lazy<VM> {
    this as? ComponentActivity ?: throw IllegalStateException()
    return viewModels<VM> { ViewModelFactory(this, create) }
}