package com.example.playlistmaker.media.model.repository

import com.example.playlistmaker.media.model.domain.FavoriteTracksInteractor
import com.example.playlistmaker.search.model.domain.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(private val repository: FavoriteTracksRepository) :
    FavoriteTracksInteractor {
    override fun getAllTracks(): Flow<List<Track>> = repository.getAllTracks()
}