package com.example.playlistmaker.media.model.domain.repository

import com.example.playlistmaker.media.model.domain.Playlist

interface PlaylistsRepository {
    fun getPlaylists():List<Playlist>
}