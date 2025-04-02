package com.chilly.android.data.remote.dto.request

import kotlinx.serialization.Serializable

@Serializable
class CommentRequest(
    val placeId: Int,
    val rating: Float,
    val commentText: String? = null
)
