package com.example.playlistmaker.player.model.domain

import com.example.playlistmaker.search.model.domain.Track
import kotlinx.coroutines.flow.Flow

interface CurrentFavoriteTrackInteractor {
    fun setCurrentTrack(trackToSetCurrent: Track)
    suspend fun addTrackToFavorites()
    suspend fun removeTrackFromFavorites()
    fun isTrackFavorite(): Flow<Boolean>
}