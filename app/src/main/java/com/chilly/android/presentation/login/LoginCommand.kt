package com.chilly.android.presentation.login

sealed interface LoginCommand {
    class LogIn(val username: String, val password: String) : LoginCommand
}