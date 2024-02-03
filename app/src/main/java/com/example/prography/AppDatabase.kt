package com.example.prography

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BookMarkPhotoEntity::class], version = 5)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookMarkPhotoDao(): BookMarkPhotoDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration() // <- 이 부분을 추가
                    .build()
                instance = newInstance
                newInstance
            }
        }
    }
}