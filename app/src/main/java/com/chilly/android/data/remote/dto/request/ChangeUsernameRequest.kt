package com.chilly.android.data.remote.dto.request

import kotlinx.serialization.Serializable

@Serializable
class ChangeUsernameRequest(
    val email: String,
    val phone: String
)