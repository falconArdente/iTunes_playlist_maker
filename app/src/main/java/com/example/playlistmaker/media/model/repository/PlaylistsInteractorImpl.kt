package com.example.playlistmaker.media.model.repository

import android.net.Uri
import com.example.playlistmaker.media.model.domain.Playlist
import com.example.playlistmaker.media.model.domain.PlaylistsInteractor
import com.example.playlistmaker.search.model.domain.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(private val repository: PlaylistsRepository) :
    PlaylistsInteractor {
    override suspend fun getAllPlaylists(): Flow<List<Playlist>> = repository.getAllPlaylists()

    override suspend fun createPlaylist(title: String, description: String, imageUri: Uri?) =
        repository
            .createPlaylist(title, description, imageUri)

    override suspend fun deletePlaylist(playlist: Playlist) {
        getTracksOfPlaylist(playlist)
            .collect { list ->
                list.forEach { track ->
                    removeTrackFromPlaylist(track, playlist)
                }
                repository.deletePlaylist(playlist)
            }
    }

    override fun addTrackToPlaylist(trackToAdd: Track, playlist: Playlist): Boolean = repository
        .addTrackToPlaylist(trackToAdd, playlist)

    override suspend fun removeTrackFromPlaylist(trackToRemove: Track, playlist: Playlist) =
        repository
            .removeTrackFromPlaylist(trackToRemove, playlist)

    override suspend fun getTracksOfPlaylist(playlist: Playlist): Flow<List<Track>> = repository
        .getTracksOfPlaylist(playlist)

    override suspend fun getPlaylistWithTracksById(playlistId: Int): Flow<Playlist> {
        return repository.getPlaylistWithTracksById(playlistId)
    }

    override suspend fun getPlaylistById(playlistId: Int): Playlist {
        return repository.getPlaylistById(playlistId)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        repository.updatePlaylist(playlist)
    }
}