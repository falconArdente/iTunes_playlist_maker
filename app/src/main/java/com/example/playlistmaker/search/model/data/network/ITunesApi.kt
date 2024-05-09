package com.example.playlistmaker.search.model.data.network

import com.example.playlistmaker.search.model.data.dto.TracksSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    suspend fun findTrack(@Query("term") searchQuery: String): TracksSearchResponse
}