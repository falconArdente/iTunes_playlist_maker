package com.example.playlistmaker

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Utility {
    private const val BASE_URL = "https://itunes.apple.com"
    fun initItunesService():ITunesApi{
        val retrofit = Retrofit.Builder()
            .baseUrl( BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ITunesApi::class.java)
    }
}