package com.example.playlistmaker.data.repository

import com.example.playlistmaker.domain.api.SearchInteractor
import com.example.playlistmaker.domain.models.Track

interface SearchRepository {
    fun searchTracks(expression: String): List<Track>
    fun setOnFailure(errorConsumer: SearchInteractor.ErrorConsumer)
    fun setOnSuccess(consumer: SearchInteractor.TracksConsumer)

}