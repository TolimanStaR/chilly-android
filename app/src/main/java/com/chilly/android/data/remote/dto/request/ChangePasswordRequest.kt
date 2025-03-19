package com.chilly.android.data.remote.dto.request

import kotlinx.serialization.Serializable

@Serializable
class ChangePasswordRequest(
    val newPassword: String,
    val oldPassword: String
)