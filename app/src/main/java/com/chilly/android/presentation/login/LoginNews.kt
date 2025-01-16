package com.chilly.android.presentation.login

sealed interface LoginNews {
    data object NavigateMain : LoginNews
}