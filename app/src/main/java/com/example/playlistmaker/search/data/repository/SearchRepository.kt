package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.Track

interface SearchRepository {
    fun searchTracks(expression: String): List<Track>
    fun setOnFailure(errorConsumer: SearchInteractor.ErrorConsumer)
    fun setOnSuccess(consumer: SearchInteractor.TracksConsumer)

}