package com.example.playlistmaker.media.model.repository

import com.example.playlistmaker.search.model.domain.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {
    suspend fun addTrackToFavorites(trackToAdd:Track)
    suspend fun removeTrackFromFavorites(trackToRemove:Track)
    suspend fun getAllTracks():Flow<List<Track>>
    fun getAllIds():Flow<List<Long>>
}