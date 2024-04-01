package com.example.playlistmaker.media.model.domain.repository

import com.example.playlistmaker.media.model.domain.Playlist
import com.example.playlistmaker.media.model.domain.PlaylistsInteractor

class PlaylistsIteractorImpl(val repository: PlaylistsRepository): PlaylistsInteractor {
    override fun providePlaylists(): List<Playlist> =repository.getPlaylists()

}