package com.example.playlistmaker.media.model.domain

import com.example.playlistmaker.search.model.domain.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {
    suspend fun addTrackToFavorites(trackToAdd: Track)
    suspend fun removeTrackFromFavorites(trackToRemove: Track)

    fun  isTrackFavorite(trackToCheck: Track):Flow<Boolean>
    fun getAllTracks(): Flow<List<Track>>
}