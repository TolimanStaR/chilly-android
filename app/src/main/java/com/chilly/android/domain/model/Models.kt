package com.chilly.android.domain.model

import com.chilly.android.data.remote.dto.PlaceDto
import java.util.Date

class HistoryItem(
    val place: PlaceDto,
    val timestamp: Date
)