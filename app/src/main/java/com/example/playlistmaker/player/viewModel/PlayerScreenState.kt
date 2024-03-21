package com.example.playlistmaker.player.viewModel

import com.example.playlistmaker.player.model.domain.PlayState
import com.example.playlistmaker.search.model.domain.Track

data class PlayerScreenState(
    var track: Track = Track(
        id = "-1", trackTitle = "Not initialized track error", "", "0", "",
        "", "", "", "", ""
    ),
    var playState: PlayState = PlayState.NotReady,
    var currentPosition: Int = 0
)
