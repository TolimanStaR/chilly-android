package com.chilly.android.presentation.common.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.chilly.android.R


@Composable
fun FormSurface(
    innerPadding: PaddingValues,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
    ) {
        PeppersBackground()
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(16.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .widthIn(
                    max = with(LocalConfiguration.current) {
                        minOf(screenWidthDp, screenHeightDp).dp
                    }
                )
        ) {
            content()
        }
    }
}

@Composable
fun CancellableTextField(
    text: String,
    @StringRes labelTextRes: Int? = null,
    @StringRes placeholderTextRes: Int? = null,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    errorText: String? = null,
    modifier: Modifier = Modifier
) {
    ChillyTextField(
        value = text,
        onValueChange = onValueChange,
        labelTextRes = labelTextRes,
        placeholderTextRes = placeholderTextRes,
        trailingIcon = {
            if (text.isNotBlank()) {
                IconButton(
                    onClick = onClear
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null
                    )
                }
            }
        },
        errorText = errorText,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun PasswordTextField(
    text: String,
    passwordShown: Boolean,
    @StringRes labelTextRes: Int,
    @StringRes placeholderTextRes: Int,
    onValueChange: (String) -> Unit,
    onVisibilityToggle: () -> Unit,
    errorText: String? = null,
    modifier: Modifier = Modifier
) {
    val passwordTransform = remember(passwordShown) {
        if (passwordShown) VisualTransformation.None else PasswordVisualTransformation()
    }
    ChillyTextField(
        value = text,
        onValueChange = onValueChange,
        labelTextRes = labelTextRes,
        placeholderTextRes = placeholderTextRes,
        trailingIcon = {
            IconButton(
                onClick = onVisibilityToggle
            ) {
                val icon = if (passwordShown)
                    R.drawable.eye_crossed_icon
                else
                    R.drawable.eye_icon
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null
                )
            }
        },
        errorText = errorText,
        visualTransformation = passwordTransform,
        modifier = modifier.fillMaxWidth()
    )
}