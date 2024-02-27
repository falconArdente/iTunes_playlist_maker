package com.example.playlistmaker.data.impl

import android.util.Log
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.data.dto.TracksSearchResponse
import com.example.playlistmaker.data.network.NetworkClient
import com.example.playlistmaker.data.repository.SearchRepository
import com.example.playlistmaker.domain.api.SearchInteractor
import com.example.playlistmaker.domain.models.Track

class SearchRepositoryImpl(private val networkClient: NetworkClient) : SearchRepository {
    private lateinit var succesConsumer: SearchInteractor.TracksConsumer
    private var errorConsumer: SearchInteractor.ErrorConsumer =
        object : SearchInteractor.ErrorConsumer {
            override fun consume() {
                Log.d("net", "Error with no action been set")
            }
        }

    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        Log.d("net", "sRepo code ${response.resultCode}")
        if (response.resultCode == 200) {
            Log.d("net", "sRepo income ${(response as TracksSearchResponse)}")
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
                    it.previewUrl ?: "0"
                )
            }
            Log.d("net", "sRepo remap $tracksList")
            succesConsumer.consume(tracksList)
            return tracksList
        } else {
            Log.d("net", "sRepo launch Error consumer")
            errorConsumer.consume()
            return emptyList()
        }
    }

    override fun setOnFailure(errorConsumer: SearchInteractor.ErrorConsumer) {
        this.errorConsumer = errorConsumer
    }

    override fun setOnSuccess(consumer: SearchInteractor.TracksConsumer) {
        succesConsumer = consumer
    }
}