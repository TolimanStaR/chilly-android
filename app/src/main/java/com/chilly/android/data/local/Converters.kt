package com.chilly.android.data.local

import androidx.room.TypeConverter
import java.util.Date

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
    fun dateToLong(value: Date?): Long? {
        value ?: return null
        return value.toInstant().toEpochMilli()
    }

    @TypeConverter
    fun dateFromLong(value: Long?): Date? {
        value ?: return null
        return Date(value)
    }

    companion object {

        private const val LIST_SEPARATOR = "<#>"
        private val SEPARATOR_REGEX = LIST_SEPARATOR.toRegex()

    }
}