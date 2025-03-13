package com.chilly.android.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.chilly.android.data.local.entity.PlaceEntity

@Dao
interface PlaceDao {

    @Upsert
    suspend fun insertPlaces(vararg places: PlaceEntity)

    @Query("SELECT * FROM places WHERE place_entity_id = :placeId")
    suspend fun findById(placeId: Int): PlaceEntity?
}
