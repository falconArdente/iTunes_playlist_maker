package com.example.playlistmaker.media.model.repository

import android.net.Uri
import com.example.playlistmaker.media.model.domain.Playlist
import com.example.playlistmaker.search.model.domain.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    suspend fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun createPlaylist(title: String, description: String = "", imageUri: Uri? = null)
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun addTrackToPlaylist(trackToAdd: Track, playlist: Playlist)
    suspend fun removeTrackFromPlaylist(trackToRemove: Track, playlist: Playlist)
    suspend fun getTracksOfPlaylist(playlist: Playlist): Flow<List<Track>>
}