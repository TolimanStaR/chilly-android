package com.chilly.android.data.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyCodeRequest(
    @SerialName("email")
    val email: String,
    @SerialName("code")
    val code: String
)
