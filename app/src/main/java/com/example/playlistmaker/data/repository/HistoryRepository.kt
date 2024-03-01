package com.example.playlistmaker.data.repository

import com.example.playlistmaker.domain.models.Track

interface HistoryRepository {
    fun addTrackToHistory(track: Track)
    fun getTracksHistory(): List<Track>
    fun clearHistory():Boolean
}