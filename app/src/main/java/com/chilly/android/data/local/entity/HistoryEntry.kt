package com.chilly.android.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "history_entries")
data class HistoryEntry(
    val placeId: Int,
    val timestamp: LocalDateTime,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("history_entry_id") val id: Int = 0,
)

data class EntryWithPlace(
    @Embedded val entry: HistoryEntry,
    @Embedded val place: PlaceEntity
)
