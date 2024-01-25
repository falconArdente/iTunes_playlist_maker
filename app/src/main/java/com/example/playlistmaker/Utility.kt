package com.example.playlistmaker

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Utility {
    fun initItunesService(context: Context): ITunesApi {
        val baseURL = context.getString(R.string.base_url_for_search)
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ITunesApi::class.java)
    }
}