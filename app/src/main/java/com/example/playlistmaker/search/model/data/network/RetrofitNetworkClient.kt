package com.example.playlistmaker.search.model.data.network

import com.example.playlistmaker.search.model.data.dto.Response
import com.example.playlistmaker.search.model.data.dto.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit

class RetrofitNetworkClient(retrofit: Retrofit) : NetworkClient {
    private val iTunesService = retrofit.create(ITunesApi::class.java)
    override suspend fun doRequest(dto: Any): Response {
        if (dto !is TracksSearchRequest) return Response().apply { resultCode = -1 }
        return withContext(Dispatchers.IO) {
            try {
                val trackResponse = iTunesService.findTrack(dto.expression)
                trackResponse.expression = dto.expression
                trackResponse.apply { resultCode = 200 }
            } catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
        }
    }
}