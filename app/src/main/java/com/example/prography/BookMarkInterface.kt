package com.example.prography

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BookMarkPhotoDao {
    @Query("SELECT * FROM bookmark_photos")
    suspend fun getAllBookmarks(): List<BookMarkPhotoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmarkPhoto: BookMarkPhotoEntity)
}