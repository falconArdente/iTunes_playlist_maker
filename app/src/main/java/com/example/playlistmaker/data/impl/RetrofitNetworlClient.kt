package com.example.playlistmaker.data.impl

import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.data.dto.TracksSearchResponse
import com.example.playlistmaker.data.network.ITunesApi
import com.example.playlistmaker.data.network.NetworkClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworlClient : NetworkClient {
    private val iTunesBaseUrl =
        "https://itunes.apple.com"//val baseURL = context.getString(R.string.base_url_for_search)
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is TracksSearchRequest) {
            val resp: retrofit2.Response<TracksSearchResponse>
            try {
                resp = iTunesService.findTrack(dto.expression).execute()
            } catch (e: Throwable) {
                return Response().apply {
                    resultCode = 400
                }
            }
            if (resp.code() != 200) {
                return Response().apply {
                    resultCode = 404
                }
            }
            val body = resp.body() ?: Response()
            return (body as TracksSearchResponse).apply {
                expression = dto.expression
                resultCode = resp.code()
            }
        } else {
            return Response().apply {
                resultCode = 400
            }
        }
    }
}