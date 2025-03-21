package com.chilly.android.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class PlaceEntity(
    @PrimaryKey
    @ColumnInfo("place_entity_id") val id: Int,
    val address: String,
    val imageUrls: List<String>,
    val name: String,
    val openHours: List<String>,
    val phone: String?,
    val rating: Float?,
    val socials: List<String>,
    val website: String?,
    val yandexMapsLink: String,
    @ColumnInfo(defaultValue = "0.0")
    val latitude: Double,
    @ColumnInfo(defaultValue = "0.0")
    val longitude: Double
)