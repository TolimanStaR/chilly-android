package com.chilly.android.data.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendCodeRequest(
    @SerialName("email")
    val email: String
)
