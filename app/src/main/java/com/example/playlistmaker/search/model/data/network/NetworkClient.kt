package com.example.playlistmaker.search.model.data.network

import com.example.playlistmaker.search.model.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}