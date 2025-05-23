package com.chilly.android.presentation.common.structure

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

@Composable
inline fun <reified VM : ViewModel> ScreenHolder(
    noinline viewModelFactory: Context.(SavedStateHandle) -> VM,
    content: @Composable VM.() -> Unit
) {
    val context = LocalContext.current
    val viewModel by context.lazyViewModel { context.viewModelFactory(it) }

    viewModel.content()
}

@Composable
inline fun <reified VM : ViewModel, C: Any> ScreenHolder(
    noinline componentFactory: @DisallowComposableCalls Context.() -> C,
    noinline viewModelFactory: C.(SavedStateHandle) -> VM,
    content: @Composable ScreenHolderViewModelScope<VM, C>.() -> Unit
) {
    val context = LocalContext.current
    val component = remember { context.componentFactory() }

    val viewModel by context.lazyViewModel { component.viewModelFactory(it) }

    val scope = remember(viewModel, component) { ScreenHolderViewModelScope(viewModel, component) }
    scope.content()
}

class ScreenHolderViewModelScope<VM : ViewModel, C :  Any>(
    val viewModel : VM,
    val component : C
)