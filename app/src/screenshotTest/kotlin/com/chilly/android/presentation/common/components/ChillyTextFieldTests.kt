package com.chilly.android.presentation.common.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.chilly.android.presentation.screens.WithTheme

@Composable
@Preview
private fun PreviewTextField(
    @PreviewParameter(ParamsProvider::class) params: TextFieldParams
) = WithTheme {
    ChillyTextField(
        value = params.text,
        onValueChange = {},
        size = params.size,
        placeholderTextRes = params.placeholderTextRes,
        modifier = Modifier.padding(10.dp)
    )
}