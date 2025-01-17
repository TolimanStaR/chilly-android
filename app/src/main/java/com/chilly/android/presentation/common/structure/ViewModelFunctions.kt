package com.chilly.android.presentation.common.structure

import android.content.Context
import androidx.compose.runtime.Composable
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