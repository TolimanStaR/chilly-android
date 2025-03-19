package com.chilly.android.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import androidx.room.Upsert
import com.chilly.android.data.local.entity.FavoriteItem
import com.chilly.android.data.local.entity.PlaceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {

    @Upsert
    suspend fun insertPlaces(vararg places: PlaceEntity)

    @Query("SELECT * FROM places WHERE place_entity_id = :placeId")
    suspend fun findById(placeId: Int): PlaceEntity?

    @Query("SELECT * FROM favorites WHERE favorite_place_id = :placeId")
    suspend fun searchInFavorites(placeId: Int): FavoriteItem?

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM places JOIN favorites ON favorite_place_id = place_entity_id")
    fun getFavorites(): Flow<List<PlaceEntity>>

    @Insert
    suspend fun markAsFavorite(item: FavoriteItem)

    @Delete
    suspend fun removeFromFavorites(item: FavoriteItem)
}
