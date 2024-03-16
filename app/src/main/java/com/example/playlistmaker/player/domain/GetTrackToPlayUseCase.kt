package com.example.playlistmaker.player.domain

import com.example.playlistmaker.search.domain.Track

interface GetTrackToPlayUseCase {
    fun getTrackToPlay(): Track
}