package com.chilly.android.data.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SignUpRequest (
    @SerialName("phoneNumber")
    val phone: String,
    @SerialName("email")
    val email: String,
    @SerialName("firstname")
    val name: String,
    @SerialName("password")
    val password: String,
    @SerialName("lastname")
    val lastname: String? = null
)
