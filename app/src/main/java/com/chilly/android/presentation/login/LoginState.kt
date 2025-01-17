package com.chilly.android.presentation.login

data class LoginState(
    val loginText: String = "",
    val passwordText: String = "",
    val loginButtonEnabled: Boolean = false,
    val passwordShown: Boolean = false,
    val isLoading: Boolean = false
)