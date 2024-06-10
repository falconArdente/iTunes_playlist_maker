package com.example.playlistmaker.player.model.repository

import com.example.playlistmaker.media.model.repository.FavoriteTracksRepository
import com.example.playlistmaker.player.model.domain.CurrentFavoriteTrackInteractor
import com.example.playlistmaker.search.model.domain.Track
import com.example.playlistmaker.utils.emptyTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CurrentFavoriteTrackInteractorImpl(
    private val repository: FavoriteTracksRepository
) : CurrentFavoriteTrackInteractor {
    private var currentTrack: Track = emptyTrack
    override fun setCurrentTrack(trackToSetCurrent: Track) {
        currentTrack = trackToSetCurrent
    }

    override suspend fun addTrackToFavorites() {
        repository.addTrackToFavorites(currentTrack)
    }

    override suspend fun removeTrackFromFavorites() {
        repository.removeTrackFromFavorites(currentTrack)
    }

    override fun isTrackFavorite(): Flow<Boolean> {
        return flow {
            repository.getAllIds().collect { listOfRemoteIds ->
                emit(listOfRemoteIds.contains(currentTrack.id))
            }
        }
    }
}