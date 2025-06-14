package com.chilly.android.data.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangeInfoRequest(
    @SerialName("firstname")
    val name: String,
    val lastname: String? = null
)