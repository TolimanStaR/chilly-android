package com.chilly.android.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UserDto(
    @SerialName("phoneNumber")
    val phone: String,
    @SerialName("email")
    val email: String,
    @SerialName("firstname")
    val name: String,
    @SerialName("lastname")
    val lastname: String
)