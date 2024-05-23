package com.example.playlistmaker.media.model.domain

import com.example.playlistmaker.search.model.domain.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {
    suspend fun getAllTracks(): Flow<List<Track>>
}