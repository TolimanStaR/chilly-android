package com.chilly.android.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.chilly.android.data.local.dao.HistoryDao
import com.chilly.android.data.local.dao.PlaceDao
import com.chilly.android.data.local.entity.FavoriteItem
import com.chilly.android.data.local.entity.HistoryEntry
import com.chilly.android.data.local.entity.PlaceEntity

@Database(
    entities = [HistoryEntry::class, PlaceEntity::class, FavoriteItem::class],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
@TypeConverters(Converters::class)
abstract class ChillyDatabase : RoomDatabase() {

    abstract val historyDao: HistoryDao

    abstract val placeDao: PlaceDao

}