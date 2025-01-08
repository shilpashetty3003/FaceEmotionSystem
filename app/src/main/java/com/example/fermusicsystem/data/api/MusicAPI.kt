package com.example.fermusicsystem.data.api

import com.example.fermusicsystem.data.model.Music
import com.example.fermusicsystem.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface MusicAPI {


    @GET("search")
    suspend fun getMusicDetails(
        @Query("q") q: String,
        @Query("key") key: String = Constants.API_KEY,
        @Query("part") part:String = "snippet",
        @Query("order") order:String = "viewCount",
        @Query("regionCode") regionCode:String = "IN",
        @Query("maxResults") maxResults :Int = 40,

    ):Response<Music>

}