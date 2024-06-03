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

    override suspend fun deletePlaylist(playlist: Playlist) = repository
        .deletePlaylist(playlist)

    override suspend fun addTrackToPlaylist(trackToAdd: Track, playlist: Playlist) = repository
        .addTrackToPlaylist(trackToAdd, playlist)

    override suspend fun removeTrackFromPlaylist(trackToRemove: Track, playlist: Playlist) =
        repository
            .removeTrackFromPlaylist(trackToRemove, playlist)

    override suspend fun getTracksOfPlaylist(playlist: Playlist): Flow<List<Track>> = repository
        .getTracksOfPlaylist(playlist)
}