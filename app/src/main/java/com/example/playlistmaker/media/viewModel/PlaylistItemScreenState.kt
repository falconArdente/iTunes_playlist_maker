package com.example.playlistmaker.media.viewModel

import android.net.Uri
import com.example.playlistmaker.search.model.domain.Track

sealed class PlaylistItemScreenState {
    data object Empty : PlaylistItemScreenState()
    data class HaveData(
        val imageUri: Uri,
        val title: String,
        val description: String,
        val minutes: Int,
        val tracksCount: Int,
        val trackList: List<Track> = emptyList(),
        val isOptionsBottomSheet: Boolean = false,
    ) : PlaylistItemScreenState()
}