package com.example.playlistmaker.search.model.data.repository

import com.example.playlistmaker.search.model.domain.ErrorConsumer
import com.example.playlistmaker.search.model.domain.SearchInteractor
import com.example.playlistmaker.search.model.domain.TracksConsumer
import java.util.concurrent.Executors

class SearchInteractorImpl(private val repository: SearchRepository) : SearchInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(
        expression: String, trackConsumer: TracksConsumer,
        errorConsumer: ErrorConsumer?
    ) {
        repository.setOnSuccess(trackConsumer)
        if (errorConsumer != null) {
            repository.setOnFailure(errorConsumer)
        }
        executor.execute {
            repository.searchTracks(expression)
        }
    }
}