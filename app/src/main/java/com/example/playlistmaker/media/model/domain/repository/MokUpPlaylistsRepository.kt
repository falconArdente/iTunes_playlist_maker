package com.example.playlistmaker.media.model.domain.repository

import com.example.playlistmaker.media.model.domain.Playlist

class MokUpPlaylistsRepository : PlaylistsRepository {
    override fun getPlaylists(): List<Playlist> {
        val playlists = mutableListOf<Playlist>()
        playlists.add(Playlist(11))
        return if (false) playlists.toList() else listOf()//waits realisation income
    }
}