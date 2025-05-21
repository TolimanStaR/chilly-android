package com.chilly.android.presentation.common.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.chilly.android.R
import com.chilly.android.presentation.theme.ChillyTheme

@Composable
@Preview
fun PreviewChillyButton(
    @PreviewParameter(PreviewButtonParamsProvider::class) params: ButtonParameters,
) {
    ChillyTheme {
        ChillyButton(
            textRes = R.string.app_name,
            onClick = {},
            enabled = params.enabled,
            color = params.color,
            size = params.size,
            type = params.type,
            modifier = Modifier.padding(10.dp)
        )
    }
}