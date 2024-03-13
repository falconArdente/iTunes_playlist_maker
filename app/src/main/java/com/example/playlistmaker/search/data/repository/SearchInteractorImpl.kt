package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.search.domain.SearchInteractor
import java.util.concurrent.Executors

class SearchInteractorImpl(private val repository: SearchRepository) : SearchInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(
        expression: String, trackConsumer: SearchInteractor.TracksConsumer,
        errorConsumer: SearchInteractor.ErrorConsumer?
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