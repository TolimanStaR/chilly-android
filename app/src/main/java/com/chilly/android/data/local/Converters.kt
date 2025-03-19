package com.chilly.android.data.local

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class Converters {

    @TypeConverter
    fun listFromString(value: String?): List<String> {
        value ?: return emptyList()
        return value.split(regex = SEPARATOR_REGEX)
    }

    @TypeConverter
    fun listToJoinedString(value: List<String>?): String? {
        value ?: return null
        return value.joinToString(separator = LIST_SEPARATOR)
    }

    @TypeConverter
    fun dateToLong(value: LocalDateTime?): Long? {
        value ?: return null
        return value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    @TypeConverter
    fun dateFromLong(value: Long?): LocalDateTime? {
        value ?: return null
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.systemDefault())
    }

    companion object {

        private const val LIST_SEPARATOR = "<#>"
        private val SEPARATOR_REGEX = LIST_SEPARATOR.toRegex()

    }
}