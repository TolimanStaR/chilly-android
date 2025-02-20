package com.chilly.android.data.remote.dto.request

import com.chilly.android.data.remote.dto.QuizAnswerDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class QuizResponseRequest(
    @SerialName("type")
    val type: String,
    @SerialName("answers")
    val answers: List<QuizAnswerDto>
)