package com.chilly.android.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.chilly.android.R

@Composable
fun PeppersBackground() {
    val backgroundImage = ImageBitmap.imageResource(R.drawable.pappers)
    val shaderBrush = remember {
        ShaderBrush(ImageShader(backgroundImage, TileMode.Repeated, TileMode.Repeated))
    }
    Box(
        modifier = Modifier
            .blur(3.dp)
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .background(shaderBrush)
            .fillMaxSize()
    )
}