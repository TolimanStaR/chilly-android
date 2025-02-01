package com.chilly.android.presentation.common.components

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.chilly.android.R
import com.chilly.android.presentation.theme.ChillyTheme

@Composable
fun ChillyButton(
    @StringRes textRes: Int,
    onClick: () -> Unit,
    enabled: Boolean = true,
    color: ChillyButtonColor = ChillyButtonColor.Primary,
    size: SizeParameter = SizeParameter.Small,
    type: ChillyButtonType = ChillyButtonType.Primary,
    modifier: Modifier = Modifier
) {
    val colors = buttonColors(type, color)
    Button(
        enabled = enabled,
        colors = colors,
        shape = RoundedCornerShape(8.dp),
        onClick = onClick,
        border = type.toBorder(colors, enabled),
        contentPadding = size.toPaddingValues(),
        modifier = modifier
    ) {
        Text(
            text = stringResource(textRes),
            style = size.toTextStyle(),
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun buttonColors(type: ChillyButtonType, color: ChillyButtonColor): ButtonColors = with(MaterialTheme.colorScheme) {
    val primaryBase = ButtonDefaults.buttonColors(
        disabledContainerColor = secondaryContainer,
        disabledContentColor = onSecondaryContainer
    )
    val otherBase = ButtonDefaults.buttonColors(
        disabledContainerColor = Color.Transparent,
        disabledContentColor = secondaryContainer
    )
    return when {
        type is ChillyButtonType.Primary && color is ChillyButtonColor.Primary ->
            primaryBase.copy(
                containerColor = primary,
                contentColor = onPrimary,
            )
        type is ChillyButtonType.Primary && color is ChillyButtonColor.Gray ->
            primaryBase.copy(
                containerColor = secondary,
                contentColor = onSecondary,
            )
        color is ChillyButtonColor.Primary -> otherBase.copy(
            containerColor = Color.Transparent,
            contentColor = primary,
        )
        color is ChillyButtonColor.Gray -> otherBase.copy(
            containerColor = Color.Transparent,
            contentColor = secondary
        )
        else -> throw IllegalArgumentException()
    }
}

private fun SizeParameter.toPaddingValues(): PaddingValues = when(this) {
    SizeParameter.Medium -> PaddingValues(vertical = 16.dp, horizontal = 24.dp)
    SizeParameter.Small -> PaddingValues(vertical = 8.dp, horizontal = 16.dp)
}

private fun ChillyButtonType.toBorder(colors: ButtonColors, enabled: Boolean): BorderStroke? = when {
    this is ChillyButtonType.Secondary && !enabled -> BorderStroke(1.dp, colors.disabledContentColor)
    this is ChillyButtonType.Secondary && enabled -> BorderStroke(1.dp, colors.contentColor)
    else -> null
}

@Composable
private fun SizeParameter.toTextStyle(): TextStyle  = when(this) {
    SizeParameter.Medium -> MaterialTheme.typography.bodyMedium
    SizeParameter.Small -> MaterialTheme.typography.bodySmall
}

sealed interface ChillyButtonColor {
    data object Primary : ChillyButtonColor
    data object Gray : ChillyButtonColor
}

sealed interface ChillyButtonType {
    data object Primary : ChillyButtonType
    data object Secondary : ChillyButtonType
    data object Tertiary : ChillyButtonType
}

@Composable
@Preview
private fun PreviewChillyButton(
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

private class PreviewButtonParamsProvider : PreviewParameterProvider<ButtonParameters> {
    override val values: Sequence<ButtonParameters>
        get() {
            val sizes = listOf(SizeParameter.Small, SizeParameter.Medium)
            val colors = listOf(ChillyButtonColor.Primary, ChillyButtonColor.Gray)
            val types = listOf(ChillyButtonType.Primary, ChillyButtonType.Secondary, ChillyButtonType.Tertiary)

            return buildList {
                sizes.forEach { size ->
                    colors.forEach { color ->
                        types.forEach { type ->
                            listOf(true, false).forEach { enabled ->
                                add(ButtonParameters(size, type, color, enabled))
                            }
                        }
                    }
                }
            }.asSequence()
        }
}

private class ButtonParameters(
    val size: SizeParameter,
    val type: ChillyButtonType,
    val color: ChillyButtonColor,
    val enabled: Boolean
)