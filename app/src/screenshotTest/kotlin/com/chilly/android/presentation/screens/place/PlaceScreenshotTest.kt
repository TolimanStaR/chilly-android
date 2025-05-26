package com.chilly.android.presentation.screens.place

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.chilly.android.presentation.screens.WithTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
@PreviewLightDark
@Preview(showBackground = true)
private fun PreviewPlaceInfoScreen()  = WithTheme {
    SharedTransitionLayout {
        AnimatedVisibility(true) {
            PlaceInfoScreen(
                state = PlaceUiState(-1),
                padding = PaddingValues(),
                onEvent = {},
                transitionScope = this@SharedTransitionLayout,
                visibilityScope = this@AnimatedVisibility
            )
        }
    }
}