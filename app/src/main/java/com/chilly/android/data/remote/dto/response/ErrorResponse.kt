package com.chilly.android.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    @SerialName("statusCode")
    val statusCode: Int,
    @SerialName("message")
    val message: String
)
