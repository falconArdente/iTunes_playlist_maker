package com.example.playlistmaker.media.model.data.db

import com.example.playlistmaker.media.model.data.db.dao.FavoriteTracksDao
import com.example.playlistmaker.media.model.repository.FavoriteTracksRepository
import com.example.playlistmaker.search.model.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteTracksRepositoryRoomImpl(private val favoritesTable: FavoriteTracksDao) :
    FavoriteTracksRepository {
    override suspend fun addTrackToFavorites(trackToAdd: Track) {
        val trackEntity = TrackDbConverter.map(trackToAdd)
        favoritesTable.deleteTrackEntity(trackEntity)
        trackEntity.dateOfChange = System.currentTimeMillis()
        favoritesTable.addTrackEntity(trackEntity)
    }

    override suspend fun removeTrackFromFavorites(trackToRemove: Track) {
        val tracks = favoritesTable.getEntityListByRemoteId(trackToRemove.id)
        if (tracks.isNotEmpty()) tracks.forEach { trackEntity ->
            favoritesTable.deleteTrackEntity(trackEntity)
        }
    }

    override suspend fun getAllTracks(): Flow<List<Track>> {
       return favoritesTable.getAll().map { listOfEntity ->
            listOfEntity.sortedByDescending { entity ->
                entity.dateOfChange
            }
        }
            .map { listOfEntity ->
                listOfEntity.map { TrackDbConverter.map(it) }
            }
    }

    override fun getAllIds(): Flow<List<Long>>{
        return favoritesTable.getRemoteIdList()
    }
}