package com.example.playlistmaker.player.viewModel

import com.example.playlistmaker.player.model.domain.PlayState
import com.example.playlistmaker.search.model.domain.Track
import com.example.playlistmaker.utils.emptyTrack

data class PlayerScreenState(
    var track: Track = emptyTrack,
    var playState: PlayState = PlayState.NotReady,
    var currentPosition: Int = 0
)
