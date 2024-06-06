package com.example.playlistmaker.media.viewModel

import com.example.playlistmaker.media.model.domain.Playlist

sealed class PlaylistScreenState {
    data object Empty : PlaylistScreenState()
    data class HaveData(val playlists: List<Playlist>) : PlaylistScreenState()
}