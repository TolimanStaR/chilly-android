package com.chilly.android.presentation.common.structure

import android.content.res.Resources

interface UiStateMapper<S: Any, UiS: Any> {
    suspend fun Resources.mapToUiState(state: S): UiS
}