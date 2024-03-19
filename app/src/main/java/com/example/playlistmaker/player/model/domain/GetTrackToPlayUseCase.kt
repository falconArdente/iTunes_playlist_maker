package com.example.playlistmaker.player.model.domain

import com.example.playlistmaker.search.model.domain.Track

interface GetTrackToPlayUseCase {
    fun getTrackToPlay(): Track
}