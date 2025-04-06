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
    val lastname: String? = null
)

@Serializable
enum class QuizType {
    BASE, SHORT
}

@Serializable
class QuestionDto(
    @SerialName("id")
    val id: Int,
    @SerialName("body")
    val body: String,
    @SerialName("answers")
    val answers: List<AnswerDto>
)

@Serializable
class AnswerDto(
    @SerialName("id")
    val id: Int,
    @SerialName("body")
    val body: String
)

@Serializable
class QuizAnswerDto(
    @SerialName("questionId")
    val questionId: Int,
    @SerialName("answerId")
     val answerId: Int
)

@Serializable
class PlaceDto(
    val id: Int,
    val address: String,
    @SerialName("images")
    val imageUrls: List<String>,
    val name: String,
    val openHours: List<String>,
    val phone: String?,
    val rating: Float?,
    @SerialName("social")
    val socials: List<String>,
    val website: String?,
    @SerialName("ypage")
    val yandexMapsLink: String,
    val latitude: Double,
    val longitude: Double
)

@Serializable
class CommentDto(
    val id: Int,
    val placeId: Int,
    val userId: Int,
    val text: String?,
    val timestamp: Long, // time milliseconds in UTC
    val rating: Float
)