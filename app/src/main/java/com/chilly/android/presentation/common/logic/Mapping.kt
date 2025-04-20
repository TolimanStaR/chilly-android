package com.chilly.android.presentation.common.logic

import android.content.res.Resources
import com.chilly.android.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit.DAYS
import java.util.Locale

fun Resources.formattedDate(date: LocalDateTime): String {
    val now = LocalDateTime.now()
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault())
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())

    val dayStr = when {
        now.truncatedTo(DAYS) == date.truncatedTo(DAYS) -> getString(R.string.today)
        now.truncatedTo(DAYS).minusDays(1) == date.truncatedTo(DAYS) -> getString(R.string.yesterday)
        else -> date.format(dateFormatter)
    }
    val timeStr = date.format(timeFormatter)

    return "$dayStr, $timeStr"
}