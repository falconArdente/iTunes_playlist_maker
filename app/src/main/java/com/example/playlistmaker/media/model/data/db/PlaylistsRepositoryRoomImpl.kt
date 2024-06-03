package com.example.playlistmaker.media.model.data.db

import android.net.Uri
import androidx.core.net.toUri
import com.example.playlistmaker.media.model.data.db.dao.PlaylistsDao
import com.example.playlistmaker.media.model.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media.model.domain.Playlist
import com.example.playlistmaker.media.model.repository.PlaylistsRepository
import com.example.playlistmaker.search.model.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryRoomImpl(private val playlistsTable: PlaylistsDao) :
    PlaylistsRepository {

    override suspend fun getAllPlaylists(): Flow<List<Playlist>> = playlistsTable.getAllPlaylists()
        .map { playlistEntitys ->
            playlistEntitys.map { entity ->
                Playlist(
                    id = entity.playlistId ?: -1,
                    title = entity.title,
                    description = entity.description ?: "",
                    imageUri = entity.imageUri?.toUri()
                )
            }
        }

    override suspend fun createPlaylist(title: String, description: String, imageUri: Uri?) =
        playlistsTable
            .createPlaylist(
                PlaylistEntity(title = title, description = description, imageUri = imageUri.toString().orEmpty())
            )

    override suspend fun deletePlaylist(playlist: Playlist) = playlistsTable
        .getPlaylistById(playlist.id)
        .collect { list ->
            list.forEach { playlistEntity ->
                playlistsTable.deletePlaylistEntity(playlistEntity)
            }
        }

    override suspend fun addTrackToPlaylist(trackToAdd: Track, playlist: Playlist) = playlistsTable
        .addPTrack(
            TrackDbConverter.map(trackToAdd, playlist)
        )

    override suspend fun removeTrackFromPlaylist(trackToRemove: Track, playlist: Playlist) {
        playlistsTable.getTracksOfPlaylist(playlist.id)
            .collect { pTrackList ->
                pTrackList.forEach { pTrackEntity ->
                    playlistsTable.removePTrack(pTrackEntity)
                }
            }
    }

    override suspend fun getTracksOfPlaylist(playlist: Playlist): Flow<List<Track>> = playlistsTable
        .getTracksOfPlaylist(playlist.id)
        .map { listOfEntity ->
            listOfEntity.map { pTrackEntity ->
                TrackDbConverter.map(pTrackEntity)
            }
        }
}