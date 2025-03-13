package com.chilly.android.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.chilly.android.data.local.entity.EntryWithPlace
import com.chilly.android.data.local.entity.HistoryEntry
import com.chilly.android.data.local.entity.PlaceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Upsert
    suspend fun insertEntry(entry: HistoryEntry)

    @Transaction
    @Query("SELECT * FROM history_entries JOIN places ON placeId = place_entity_id ORDER BY timestamp DESC")
    fun getHistory(): Flow<List<EntryWithPlace>>
}
