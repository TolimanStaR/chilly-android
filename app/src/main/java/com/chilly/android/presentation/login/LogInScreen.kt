package com.chilly.android.presentation.login

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.chilly.android.R
import com.chilly.android.applicationComponent
import com.chilly.android.di.screens.DaggerLoginComponent
import com.chilly.android.di.screens.LoginComponent
import com.chilly.android.presentation.common.components.ChillyButton
import com.chilly.android.presentation.common.components.ChillyButtonType
import com.chilly.android.presentation.common.components.ChillyTextField
import com.chilly.android.presentation.common.components.SizeParameter
import com.chilly.android.presentation.common.structure.NewsCollector
import com.chilly.android.presentation.common.structure.ScreenHolder
import com.chilly.android.presentation.login.LoginEvent.UiEvent
import com.chilly.android.presentation.navigation.Destination
import com.chilly.android.presentation.navigation.clearStackAndNavigate
import com.chilly.android.presentation.theme.ChillyTheme
import com.chilly.android.presentation.theme.Gray20
import kotlinx.coroutines.flow.FlowCollector

@Composable
private fun LogInScreen(state: LoginState, onEvent: (UiEvent) -> Unit) {
    Scaffold { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
        ) {
            PepperBackground()
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                Text(
                    text = stringResource(R.string.login_title),
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                ChillyTextField(
                    value = state.loginText,
                    onValueChange = { onEvent(UiEvent.LoginChanged(it)) },
                    labelTextRes = R.string.username_label,
                    placeholderTextRes = R.string.username_placeholder,
                    trailingIcon = {
                        if (state.loginText.isNotBlank()) {
                            IconButton(
                                onClick = {}
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                ChillyTextField(
                    value = state.passwordText,
                    onValueChange = { onEvent(UiEvent.PasswordChanged(it)) },
                    labelTextRes = R.string.password_label,
                    placeholderTextRes = R.string.password_placeholder,
                    trailingIcon = {
                        if (state.loginText.isNotBlank()) {
                            IconButton(
                                onClick = {}
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.eye_icon),
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                ChillyButton(
                    textRes = R.string.forgot_password_button,
                    type = ChillyButtonType.Tertiary,
                    onClick = {},
                )
                Spacer(modifier = Modifier.height(36.dp))
                ChillyButton(
                    textRes = R.string.login_button,
                    size = SizeParameter.Medium,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onEvent(UiEvent.LogInClicked) }
                )
                Spacer(modifier = Modifier.height(12.dp))
                ChillyButton(
                    textRes = R.string.sign_up_button,
                    size = SizeParameter.Medium,
                    type = ChillyButtonType.Secondary,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {}
                )
            }
        }
    }
}

@Composable
private fun PepperBackground() {
    val backgroundImage = ImageBitmap.imageResource(R.drawable.pappers)
    val shaderBrush = remember(backgroundImage) {
        ShaderBrush(ImageShader(backgroundImage, TileMode.Repeated, TileMode.Repeated))
    }
    Box(
        modifier = Modifier
            .blur(3.dp)
            .background(color = Gray20)
            .background(shaderBrush)
            .fillMaxSize()
    )
}

fun NavGraphBuilder.logInScreenComposable(navController: NavController) {
    composable<Destination.LogIn> {
        ScreenHolder(
            storeFactory = {
                buildComponent().storeFactory().build()
            }
        ) {
            val state = state.collectAsStateWithLifecycle()
            LogInScreen(state.value, ::dispatch)
            NewsCollector(createNewsCollector(navController))
        }
    }
}

private fun Context.buildComponent(): LoginComponent = DaggerLoginComponent.builder()
    .appComponent(applicationComponent)
    .build()

private fun createNewsCollector(navController: NavController) = FlowCollector { news: LoginNews ->
    when(news) {
        LoginNews.NavigateMain -> navController.clearStackAndNavigate(Destination.Main)
    }
}

@Composable
@Preview(name = "login screen", showSystemUi = true, showBackground = true)
private fun PreviewLoginScreen() {
    ChillyTheme {
        LogInScreen(
            LoginState(
                loginText = "login",
                passwordText = "password"
            )
        ) { }
    }
}

