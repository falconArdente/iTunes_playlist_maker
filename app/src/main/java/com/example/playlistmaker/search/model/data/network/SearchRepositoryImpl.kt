package com.example.playlistmaker.search.model.data.network

import android.app.Application
import com.example.playlistmaker.R
import com.example.playlistmaker.search.model.data.dto.TracksSearchRequest
import com.example.playlistmaker.search.model.data.dto.TracksSearchResponse
import com.example.playlistmaker.search.model.data.repository.SearchRepository
import com.example.playlistmaker.search.model.domain.Track
import com.example.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val application: Application
) : SearchRepository {
    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == 200) {
            (response as TracksSearchResponse)
            val tracksList: List<Track> = response.results.map {
                val idToAdd = try {
                    it.remoteId.toLong()
                } catch (e: Throwable) {
                    0L
                }
                Track(
                    idToAdd,
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
            emit(Resource.Success(tracksList))
        } else {
            emit(Resource.Error(application.getString(R.string.network_error)))
        }
    }
}