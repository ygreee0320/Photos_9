package com.example.prography

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BookMarkPhotoDao {
    @Query("SELECT * FROM bookmark_photo")
    fun getAllBookmarks(): List<BookMarkPhotoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBookmark(bookmarkPhoto: BookMarkPhotoEntity)

    @Query("SELECT * FROM bookmark_photo WHERE photoId = :id")
    fun getBookmarkById(id: String): BookMarkPhotoEntity?

    @Query("DELETE FROM bookmark_photo WHERE photoId = :id")
    fun deleteBookmarkById(id: String)
}