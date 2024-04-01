package com.example.playlistmaker.media.model.domain

interface GetPlaylistsUseCase {
    fun providePlaylists(): List<Playlist>
}