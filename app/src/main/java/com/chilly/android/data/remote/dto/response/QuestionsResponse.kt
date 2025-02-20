package com.chilly.android.data.remote.dto.response

import com.chilly.android.data.remote.dto.QuestionDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class QuestionsResponse(
    @SerialName("type")
    val type: String,
    @SerialName("questions")
    val questions: List<QuestionDto>
)

