package com.example.playlistmaker.player.model.repository

import com.example.playlistmaker.search.model.domain.Track

interface GetTrackToPlayRepository {
    fun getTrack():Track
}