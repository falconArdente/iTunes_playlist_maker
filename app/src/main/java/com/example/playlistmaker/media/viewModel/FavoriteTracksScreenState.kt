package com.example.playlistmaker.media.viewModel

import com.example.playlistmaker.search.model.domain.Track

sealed class FavoriteTracksScreenState {
    data object NoTracks : FavoriteTracksScreenState()
    data class HaveTracks(val tracks: List<Track>) : FavoriteTracksScreenState()
}