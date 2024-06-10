package com.example.playlistmaker.media.model.domain

import android.net.Uri
import com.example.playlistmaker.search.model.domain.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    suspend fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun createPlaylist(title: String, description: String = "", imageUri: Uri? = null)
    suspend fun deletePlaylist(playlist: Playlist)
    fun addTrackToPlaylist(trackToAdd: Track, playlist: Playlist):Boolean
    suspend fun removeTrackFromPlaylist(trackToRemove: Track, playlist: Playlist)
    suspend fun getTracksOfPlaylist(playlist: Playlist): Flow<List<Track>>
}