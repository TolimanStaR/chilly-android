package com.chilly.android.presentation.common.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.chilly.android.R
import com.chilly.android.presentation.theme.ChillyTheme

@Composable
fun ChillyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes labelTextRes: Int? = null,
    @StringRes placeholderTextRes: Int? = null,
    size: SizeParameter = SizeParameter.Small,
    singleLine: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var text by remember(value) { mutableStateOf(value) }
    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            onValueChange(it)
        },
        shape = RoundedCornerShape(8.dp),
        textStyle = size.toTextStyle(),
        colors = with(MaterialTheme.colorScheme) {
            OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = outline,
                focusedBorderColor = primary,
                disabledBorderColor = outlineVariant,
                unfocusedTextColor = onSurface,
                focusedTextColor = onSurface,
            )
        },
        placeholder = placeholderTextRes?.let {
            {
                Text(
                    text = stringResource(it),
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = FontStyle.Italic
                )
            }
        },
        label = labelTextRes?.let {
            {
                Text(
                    text = stringResource(it),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        },
        singleLine = singleLine,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        modifier = modifier
    )
}

@Composable
private fun SizeParameter.toTextStyle(): TextStyle = when(this) {
    SizeParameter.Medium -> MaterialTheme.typography.bodyMedium
    SizeParameter.Small -> MaterialTheme.typography.bodySmall
}

@Composable
@Preview
private fun PreviewTextField(
    @PreviewParameter(ParamsProvider::class) params: TextFieldParams
) {
    ChillyTheme {
        ChillyTextField(
            value = params.text,
            onValueChange = {},
            size = params.size,
            placeholderTextRes = params.placeholderTextRes,
            modifier = Modifier.padding(10.dp)
        )
    }
}

private class ParamsProvider : PreviewParameterProvider<TextFieldParams> {
    override val values: Sequence<TextFieldParams>
        get() {
            val sizes = listOf(SizeParameter.Small, SizeParameter.Medium)
            val texts = listOf("", "text")

            return buildList {
                sizes.forEach { size ->
                    texts.forEach { text ->
                        val placeholder = if (text.isBlank()) R.string.app_name else null
                        add(TextFieldParams(size, text, placeholder))
                    }
                }
            }.asSequence()
        }
}

private class TextFieldParams(
    val size: SizeParameter,
    val text: String,
    val placeholderTextRes: Int?
)