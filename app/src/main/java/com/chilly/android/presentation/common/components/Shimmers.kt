package com.chilly.android.presentation.common.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

@Composable
fun animatedAlpha(from: Float = 1f, to: Float = 0.5f, duration: Int = 700): State<Float> {
    val transition = rememberInfiniteTransition("pulsating_transition")
    return transition.animateFloat(
        initialValue = from,
        targetValue = to,
        label = "alpha_transition",
        animationSpec = infiniteRepeatable(
            animation = tween(duration),
            repeatMode = RepeatMode.Reverse
        )
    )
}