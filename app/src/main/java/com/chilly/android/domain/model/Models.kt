package com.chilly.android.domain.model

import com.chilly.android.data.remote.dto.PlaceDto
import java.time.LocalDateTime

class HistoryItem(
    val place: PlaceDto,
    val timestamp: LocalDateTime
)