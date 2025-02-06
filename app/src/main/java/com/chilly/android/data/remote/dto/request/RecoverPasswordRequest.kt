package com.chilly.android.data.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecoverPasswordRequest (
    @SerialName("email")
    val email: String,
    @SerialName("code")
    val code: String,
    @SerialName("newPassword")
    val password: String,
)
