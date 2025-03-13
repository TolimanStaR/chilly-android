package com.chilly.android.di.application

import android.content.Context
import androidx.room.Room
import com.chilly.android.data.local.ChillyDatabase
import com.chilly.android.data.local.dao.HistoryDao
import com.chilly.android.data.local.dao.PlaceDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(applicationContext: Context): ChillyDatabase =
        Room.databaseBuilder(
            context = applicationContext,
            klass = ChillyDatabase::class.java,
            name = "app_db"
        ).build()

    @Provides
    @Singleton
    fun providePlaceDao(db: ChillyDatabase): PlaceDao = db.placeDao

    @Provides
    @Singleton
    fun provideHistoryDao(db: ChillyDatabase): HistoryDao = db.historyDao
}