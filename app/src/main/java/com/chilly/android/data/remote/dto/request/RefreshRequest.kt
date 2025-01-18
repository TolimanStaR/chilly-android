package com.chilly.android.data.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class RefreshRequest(
    @SerialName("token")
    val token: String
)
