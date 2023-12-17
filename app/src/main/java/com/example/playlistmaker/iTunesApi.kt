package com.example.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    fun findTrack(@Query("term") searchQuery: String): Call<TrackSearchResponse>
}
