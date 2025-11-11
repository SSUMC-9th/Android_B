package com.example.week2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AlbumEntity::class], version = 1, exportSchema = false)
abstract class AlbumDatabase : RoomDatabase() {

    abstract fun albumDao(): AlbumDao

    companion object {
        @Volatile
        private var INSTANCE: AlbumDatabase? = null

        fun getInstance(context: Context): AlbumDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlbumDatabase::class.java,
                    "album_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}