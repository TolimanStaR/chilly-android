package com.chilly.android.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.chilly.android.presentation.navigation.Destination

@Composable
fun MainScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("main screen")
    }
}

fun NavGraphBuilder.mainScreenComposable() {
    composable<Destination.Main> {
        MainScreen()
    }
}