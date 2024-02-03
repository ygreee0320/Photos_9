package com.example.prography

import androidx.room.Entity
import androidx.room.PrimaryKey

data class UnsplashPhoto(
    val id: String,
    val urls: UnsplashUrls
)

data class UnsplashUrls(
    val regular: String
)

// BookMarkPhotoEntity.kt
@Entity(tableName = "bookmark_photo")
data class BookMarkPhotoEntity(
    @PrimaryKey val photoId: String,
    val imageUrl: String
)