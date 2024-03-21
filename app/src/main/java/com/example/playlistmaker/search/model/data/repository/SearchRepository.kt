package com.example.playlistmaker.search.model.data.repository

import com.example.playlistmaker.search.model.domain.ErrorConsumer
import com.example.playlistmaker.search.model.domain.Track
import com.example.playlistmaker.search.model.domain.TracksConsumer

interface SearchRepository {
    fun searchTracks(expression: String): List<Track>
    fun setOnFailure(errorConsumer: ErrorConsumer)
    fun setOnSuccess(consumer: TracksConsumer)

}