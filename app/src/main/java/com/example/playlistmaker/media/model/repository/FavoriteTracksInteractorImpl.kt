package com.example.playlistmaker.media.model.repository

import com.example.playlistmaker.media.model.domain.FavoriteTracksInteractor
import com.example.playlistmaker.search.model.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksInteractorImpl(private val repository: FavoriteTracksRepository) :
    FavoriteTracksInteractor {

    override suspend fun addTrackToFavorites(trackToAdd: Track) {
        repository.addTrackToFavorites(trackToAdd)
    }

    override suspend fun removeTrackFromFavorites(trackToRemove: Track) {
        repository.removeTrackFromFavorites(trackToRemove)
    }

    override fun isTrackFavorite(trackToCheck: Track): Flow<Boolean> = flow {
        repository
            .getAllIds()
            .collect {listOfRemoteIds->
                listOfRemoteIds.forEach {
                    emit (it==trackToCheck.id)
                }
            }
    }

    override fun getAllTracks(): Flow<List<Track>> = repository.getAllTracks()
}