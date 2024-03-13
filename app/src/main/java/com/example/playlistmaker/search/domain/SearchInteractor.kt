package com.example.playlistmaker.search.domain

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