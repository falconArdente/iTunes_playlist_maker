package com.example.playlistmaker.media.viewModel

import com.example.playlistmaker.search.model.domain.Track

sealed class CreatePlaylistScreenState {
    data object NoTracks : CreatePlaylistScreenState()
    data class HaveTracks(val tracks: List<Track>) : CreatePlaylistScreenState()
}