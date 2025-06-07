package com.chilly.android.presentation.screens.favorites

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.chilly.android.presentation.screens.WithTheme

@Composable
@PreviewLightDark
@Preview(name = "FavoritesScreen", showSystemUi = true, showBackground = true)
private fun PreviewFavoritesScreen() = WithTheme {
    FavoritesScreen(
        state = FavoritesState(),
        padding = PaddingValues(),
        onEvent = {}
    )
}