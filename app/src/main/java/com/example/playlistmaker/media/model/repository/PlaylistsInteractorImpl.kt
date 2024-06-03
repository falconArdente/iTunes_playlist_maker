package com.example.playlistmaker.media.model.repository

import android.net.Uri
import com.example.playlistmaker.media.model.domain.Playlist
import com.example.playlistmaker.media.model.domain.PlaylistsInteractor
import com.example.playlistmaker.search.model.domain.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(private val repository: FavoriteTracksRepository) :
    PlaylistsInteractor {
    override suspend fun getAllPlaylists(): Flow<List<Playlist>> {
        TODO("Not yet implemented")
    }

    override fun createPlaylist(title: String, description: String, imageUri: Uri?) {
        TODO("Not yet implemented")
    }

    override fun deletePlaylist(playlist: Playlist) {
        TODO("Not yet implemented")
    }

    override fun addTrackToPlaylist(trackToAdd: Track, playlist: Playlist) {
        TODO("Not yet implemented")
    }

    override fun removeTrackFromPlaylist(trackToRemove: Track, playlist: Playlist) {
        TODO("Not yet implemented")
    }

    override fun getTracksOfPlaylist(playlist: Playlist): Flow<List<Track>> {
        TODO("Not yet implemented")
    }
}