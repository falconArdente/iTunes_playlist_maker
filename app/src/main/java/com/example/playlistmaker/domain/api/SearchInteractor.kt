package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface SearchInteractor {
    fun searchTracks(
        expression: String,
        trackConsumer: TracksConsumer,
        errorConsumer: ErrorConsumer?
    )

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>)
    }

    interface ErrorConsumer {
        fun consume()
    }
}