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

private val DarkColorScheme = darkColorScheme(
    primary = Red50,
    secondary = Red70,
    onPrimary = Color.White,
    onSecondary = Color.White,
    inverseSurface = Gray20, // disabled
    inverseOnSurface = Color.White, // on disabled
    tertiaryContainer = Gray50,
    onTertiaryContainer = Color.White,
    secondaryContainer = Gray70,
    background = Gray70,
    onBackground = Color.White,
    outline = Gray20,
    outlineVariant = Gray10
)

private val LightColorScheme = lightColorScheme(
    primary = Red50,
    secondary = Red70,
    onPrimary = Color.White,
    inverseSurface = Gray20, // disabled
    inverseOnSurface = Color.White, // on disabled
    tertiaryContainer = Gray50,
    onTertiaryContainer = Color.White,
    secondaryContainer = Gray70, // darker than tertiary
    background = Color.White,
    onBackground = Gray90,
    outline = Gray20,
    outlineVariant = Gray10
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