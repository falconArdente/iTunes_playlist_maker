package com.example.playlistmaker.search.model.data.network

import android.util.Log
import com.example.playlistmaker.search.model.data.dto.TracksSearchRequest
import com.example.playlistmaker.search.model.data.dto.TracksSearchResponse
import com.example.playlistmaker.search.model.data.repository.SearchRepository
import com.example.playlistmaker.search.model.domain.ErrorConsumer
import com.example.playlistmaker.search.model.domain.Track
import com.example.playlistmaker.search.model.domain.TracksConsumer

class SearchRepositoryImpl(private val networkClient: NetworkClient) : SearchRepository {
    private var successConsumer: TracksConsumer? = null
    private var errorConsumer: ErrorConsumer =
        object : ErrorConsumer {
            override fun consume() {
                Log.e("net", "Error with no action been set")
            }
        }

    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == 200) {
            (response as TracksSearchResponse)
            val tracksList: List<Track> = response.results.map {
                Track(
                    it.id.orEmpty(),
                    it.trackName.orEmpty(),
                    it.artistName.orEmpty(),
                    it.trackTimeMillis.orEmpty(),
                    it.artworkUrl100.orEmpty(),
                    it.collectionName.orEmpty(),
                    it.releaseDate.orEmpty(),
                    it.primaryGenreName.orEmpty(),
                    it.country.orEmpty(),
                    it.previewUrl.orEmpty(),
                )
            }
            successConsumer?.consume(tracksList)
            return tracksList
        } else {
            errorConsumer.consume()
            return emptyList()
        }
    }

    override fun setOnFailure(errorConsumer: ErrorConsumer) {
        this.errorConsumer = errorConsumer
    }

    override fun setOnSuccess(consumer: TracksConsumer) {
        successConsumer = consumer
    }
}