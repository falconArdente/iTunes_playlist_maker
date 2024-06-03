package com.example.playlistmaker.media.model.domain

import android.net.Uri
import com.example.playlistmaker.search.model.domain.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    suspend fun getAllPlaylists(): Flow<List<Playlist>>
    fun createPlaylist(title: String, description: String = "", imageUri: Uri? = null)
    fun deletePlaylist(playlist: Playlist)
    fun addTrackToPlaylist(trackToAdd: Track, playlist: Playlist)
    fun removeTrackFromPlaylist(trackToRemove: Track, playlist: Playlist)
    fun getTracksOfPlaylist(playlist: Playlist): Flow<List<Track>>
}