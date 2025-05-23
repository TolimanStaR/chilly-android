package com.chilly.android.presentation.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import com.chilly.android.R
import com.chilly.android.activityComponent
import com.chilly.android.di.screens.DaggerProfileComponent
import com.chilly.android.di.screens.ProfileComponent
import com.chilly.android.presentation.common.components.ChillyButton
import com.chilly.android.presentation.common.components.ChillyButtonColor
import com.chilly.android.presentation.common.components.ChillyButtonType
import com.chilly.android.presentation.common.components.ChillyTextField
import com.chilly.android.presentation.common.components.LoadingPlaceholder
import com.chilly.android.presentation.common.components.PasswordTextField
import com.chilly.android.presentation.common.structure.NewsCollector
import com.chilly.android.presentation.common.structure.ScreenHolder
import com.chilly.android.presentation.common.structure.collectState
import com.chilly.android.presentation.navigation.Destination
import com.chilly.android.presentation.navigation.peekingComposable
import com.chilly.android.presentation.screens.profile.ProfileEvent.UiEvent
import com.chilly.android.presentation.theme.ChillyTheme

@Composable
private fun ProfileScreen(
    state: ProfileState,
    padding: PaddingValues,
    onEvent: (UiEvent) -> Unit
) {
    if (state.user == null) {
        LoadingPlaceholder(stringResource(R.string.user_loading)) {
            onEvent(UiEvent.ShownLoadingScreen)
        }
        return
    }

    Column(
        modifier = Modifier
            .padding(padding)
            .padding(16.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.height(4.dp)) // so the shadow is not clipped
        ProfileCard {
            ChillyTextField(
                value = state.nameText,
                onValueChange = { onEvent(UiEvent.NameTextChanged(it)) },
                modifier = Modifier.fillMaxWidth()
            )
            ChillyTextField(
                value = state.emailText,
                onValueChange = { onEvent(UiEvent.EmailTextChanged(it)) },
                errorText = state.emailErrorTextRes?.let { stringResource(it) },
                modifier = Modifier.fillMaxWidth()
            )
            ChillyTextField(
                value = state.phoneText,
                onValueChange = { onEvent(UiEvent.PhoneTextChanged(it)) },
                errorText = state.phoneErrorTextRes?.let { stringResource(it) },
                modifier = Modifier.fillMaxWidth()
            )
            ChillyButton(
                textRes = R.string.change_info_button,
                onClick = { onEvent(UiEvent.ChangeDataClicked) },
                enabled = state.changeDataEnabled && !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            )
        }
        ProfileCard {
            PasswordTextField(
                text = state.oldPasswordText,
                labelTextRes = R.string.old_password_label,
                placeholderTextRes = R.string.old_password_label,
                onVisibilityToggle = { onEvent(UiEvent.OldPasswordVisibilityToggled) },
                passwordShown = state.oldPasswordShown,
                onValueChange = { onEvent(UiEvent.OldPasswordChanged(it)) },
                modifier = Modifier.fillMaxWidth()
            )
            PasswordTextField(
                text = state.newPasswordText,
                labelTextRes = R.string.new_password_label,
                placeholderTextRes = R.string.new_password_label,
                onVisibilityToggle = { onEvent(UiEvent.NewPasswordVisibilityToggled) },
                errorText = state.newPasswordErrorRes?.let { stringResource(it) },
                passwordShown = state.newPasswordShown,
                onValueChange = { onEvent(UiEvent.NewPasswordChanged(it)) },
                modifier = Modifier.fillMaxWidth()
            )
            PasswordTextField(
                text = state.repeatPasswordText,
                labelTextRes = R.string.repeat_password_label,
                placeholderTextRes = R.string.repeat_password_label,
                onVisibilityToggle = { onEvent(UiEvent.RepeatPasswordVisibilityToggled) },
                errorText = state.repeatPasswordErrorRes?.let { stringResource(it) },
                passwordShown = state.repeatPasswordShown,
                onValueChange = { onEvent(UiEvent.RepeatPasswordChanged(it)) },
                modifier = Modifier.fillMaxWidth()
            )
            ChillyButton(
                textRes = R.string.change_passowrd_button,
                enabled = state.changePasswordEnabled && !state.isLoading,
                onClick = { onEvent(UiEvent.ChangePasswordClicked) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        ChillyButton(
            textRes = R.string.redo_onboarding_button,
            type = ChillyButtonType.Secondary,
            onClick = { onEvent(UiEvent.ShowOnboardingClicked) },
            modifier = Modifier.fillMaxWidth()
        )
        ChillyButton(
            textRes = R.string.clear_intersts_button,
            type = ChillyButtonType.Secondary,
            onClick = { onEvent(UiEvent.ClearInterestsClicked) },
            modifier = Modifier.fillMaxWidth()
        )
        ChillyButton(
            textRes = R.string.log_out_button,
            type = ChillyButtonType.Tertiary,
            color = ChillyButtonColor.Gray,
            onClick = { onEvent(UiEvent.LogOutClicked) },
            modifier = Modifier.fillMaxWidth()
        )
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun ProfileCard(content: @Composable ColumnScope.() -> Unit) {
    ElevatedCard (
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .widthIn(
                max = with(LocalConfiguration.current) {
                    minOf(screenWidthDp, screenHeightDp).dp
                }
            )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            content()
        }
    }
}

fun NavGraphBuilder.installProfileScreen(padding: PaddingValues) {
    peekingComposable<Destination.Profile> {
        ScreenHolder<ProfileStore, ProfileComponent>(
            componentFactory = {
                DaggerProfileComponent.builder()
                    .appComponent(activityComponent)
                    .build()
            },
            storeFactory = { store() }
        ) {
            val state = collectState()
            NewsCollector(component.newsCollector)
            ProfileScreen(state.value, padding, store::dispatch)
        }
    }
}

@Composable
@PreviewLightDark
@Preview(name = "ProfileScreen", showSystemUi = true, showBackground = true)
private fun PreviewProfileScreen() {
    ChillyTheme {
        Surface {
            ProfileScreen(
                state = ProfileState(),
                padding = PaddingValues(),
                onEvent = {}
            )
        }
    }
}

