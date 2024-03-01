package com.example.playlistmaker.data.impl

import android.util.Log
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.data.dto.TracksSearchResponse
import com.example.playlistmaker.data.network.NetworkClient
import com.example.playlistmaker.data.repository.SearchRepository
import com.example.playlistmaker.domain.api.SearchInteractor
import com.example.playlistmaker.domain.models.Track

class SearchRepositoryImpl(private val networkClient: NetworkClient) : SearchRepository {
    private var successConsumer: SearchInteractor.TracksConsumer? = null
    private var errorConsumer: SearchInteractor.ErrorConsumer =
        object : SearchInteractor.ErrorConsumer {
            override fun consume() {
                Log.d("net", "Error with no action been set")
            }
        }

    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == 200) {
            (response as TracksSearchResponse)
            val tracksList: List<Track> = response.results.map {
                Track(
                    it.id ?: "0",
                    it.trackName ?: "0",
                    it.artistName ?: "0",
                    it.trackTimeMillis ?: "0",
                    it.artworkUrl100 ?: "0",
                    it.collectionName ?: "0",
                    it.releaseDate ?: "0",
                    it.primaryGenreName ?: "0",
                    it.country ?: "0",
                    it.previewUrl ?: "0",
                )
            }
            successConsumer?.consume(tracksList)
            return tracksList
        } else {
            errorConsumer.consume()
            return emptyList()
        }
    }

    override fun setOnFailure(errorConsumer: SearchInteractor.ErrorConsumer) {
        this.errorConsumer = errorConsumer
    }

    override fun setOnSuccess(consumer: SearchInteractor.TracksConsumer) {
        successConsumer = consumer
    }
}