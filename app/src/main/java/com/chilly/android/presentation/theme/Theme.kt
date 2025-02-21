package com.chilly.android.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Red50, // red buttons
    onPrimary = Color.White,
    primaryContainer = Red10,
    onPrimaryContainer = Red70,
    secondary = Gray50, // gray buttons
    onSecondary = Color.White,
    secondaryContainer = Gray20, // disabled elements
    onSecondaryContainer = Color.White,
    outline = Gray20,
    outlineVariant = Gray10,

    surface = Color.White,
    onSurface = Gray90,
    background = Color.White,
    onBackground = Gray90,
    surfaceContainer = Gray10,
    onSurfaceVariant = Gray70,
    surfaceBright = Color.White,
    surfaceDim = Gray10,

    error = Red70,
    errorContainer = Red5
)

private val DarkColorScheme = darkColorScheme(
    primary = Red50, // buttons
    onPrimary = Color.White,
    primaryContainer = Red10,
    onPrimaryContainer = Red70,
    secondary = Gray40, // gray buttons
    onSecondary = Color.White,
    secondaryContainer = Gray20, // disabled elements
    onSecondaryContainer = Color.White,
    outline = Gray20,
    outlineVariant = Gray10,

    surface = Gray90,
    onSurface = Color.White,
    background = Gray90,
    onBackground = Color.White,
    surfaceContainer = Gray70,
    onSurfaceVariant = Gray20,
    surfaceBright = Gray70,
    surfaceDim = Gray90,

    error = Red70,
    errorContainer = Color.Transparent
)

@Composable
fun ChillyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val isDynamic = dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val context = LocalContext.current
    val colorScheme = when {
        isDynamic && darkTheme -> dynamicDarkColorScheme(context)
        isDynamic && !darkTheme -> dynamicLightColorScheme(context)
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}