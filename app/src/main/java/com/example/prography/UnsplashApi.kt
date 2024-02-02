package com.example.prography

import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApi {
    @GET("photos")
    suspend fun getRandomPhotos(
        @Query("client_id") clientId: String,
        @Query("page") page: Int
    ): List<UnsplashPhoto>
}