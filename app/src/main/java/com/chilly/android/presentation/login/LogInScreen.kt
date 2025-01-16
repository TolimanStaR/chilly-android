package com.chilly.android.presentation.login

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.chilly.android.applicationComponent
import com.chilly.android.di.screens.DaggerLoginComponent
import com.chilly.android.di.screens.LoginComponent
import com.chilly.android.presentation.common.NewsCollector
import com.chilly.android.presentation.common.ScreenHolder
import com.chilly.android.presentation.login.LoginEvent.UiEvent
import com.chilly.android.presentation.navigation.Destination
import kotlinx.coroutines.flow.FlowCollector

@Composable
private fun LogInScreen(state: LoginState, onEvent: (UiEvent) -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
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

@Composable
private fun CachingTextField(value: String, onChange: (String) -> Unit) {
    var text by remember { mutableStateOf(value) }
    TextField(
        value = text,
        onValueChange = {
            text = it
            onChange(it)
        }
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

