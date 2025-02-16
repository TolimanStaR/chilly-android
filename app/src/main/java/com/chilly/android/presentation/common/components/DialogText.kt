package com.chilly.android.presentation.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chilly.android.R
import com.chilly.android.presentation.theme.ChillyTheme
import com.chilly.android.presentation.theme.Gray90

@Composable
fun TextInDialogWindow(
    text: String,
    fromLeft: Boolean = false,
    verticalPaddingScale: Float = 1f,
    horizontalPaddingScale: Float = 1f
) {
    Box {
        val imageRes = if (isSystemInDarkTheme())
            R.drawable.dialog_window_right_dark
        else
            R.drawable.dialog_window_right

        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer(scaleX = if (fromLeft) -1f else 1f)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = (24 * horizontalPaddingScale).dp)
                .padding(bottom = (30 * verticalPaddingScale).dp, top = (12 * verticalPaddingScale).dp)
        )
    }
}

@Composable
@Preview
private fun PreviewLoginScreen() {
    ChillyTheme {
        TextInDialogWindow("Some")
    }
}

