package com.chilly.android.data.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LoginRequest (
    @SerialName("username")
    val username: String,
    @SerialName("password")
    val password: String
)