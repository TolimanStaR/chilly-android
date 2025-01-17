package com.chilly.android.presentation.login

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.imageResource
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
import com.chilly.android.presentation.common.structure.NewsCollector
import com.chilly.android.presentation.common.structure.ScreenHolder
import com.chilly.android.presentation.login.LoginEvent.UiEvent
import com.chilly.android.presentation.navigation.Destination
import kotlinx.coroutines.flow.FlowCollector

@Composable
private fun LogInScreen(state: LoginState, onEvent: (UiEvent) -> Unit) {
    Scaffold { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
        ) {
            PepperBackground()
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                CachingTextField(
                    value = state.loginText,
                    onChange = { onEvent(UiEvent.LoginChanged(it)) }
                )
                CachingTextField(
                    value = state.passwordText,
                    onChange = { onEvent(UiEvent.PasswordChanged(it)) }
                )
                Button(
                    onClick = { onEvent(UiEvent.LogInClicked) }
                ) {
                    Text("Log In")
                }
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
            .background(shaderBrush)
            .fillMaxSize()
    )
}

@Composable
private fun CachingTextField(value: String, onChange: (String) -> Unit) {
    var text by remember { mutableStateOf(value) }
    TextField(
        value = text,
        onValueChange = {
            text = it
            onChange(it)
        },
        modifier = Modifier.fillMaxWidth()
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
        LoginNews.NavigateMain -> navController.navigate(Destination.Main)
    }
}

@Composable
@Preview(name = "login screen", showSystemUi = true, showBackground = true)
private fun PreviewLoginScreen() {
    LogInScreen(
        LoginState(
            loginText = "login",
            passwordText = "password"
        )
    ) { }
}

