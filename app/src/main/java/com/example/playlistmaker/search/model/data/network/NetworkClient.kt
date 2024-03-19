package com.example.playlistmaker.search.model.data.network

import com.example.playlistmaker.search.model.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response

}