package com.example.playlistmaker.search.model.domain

interface SearchInteractor {
    fun searchTracks(
        expression: String,
        trackConsumer: TracksConsumer,
        errorConsumer: ErrorConsumer?
    )
}