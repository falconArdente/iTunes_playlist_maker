package com.example.playlistmaker.search.model.data.repository

import com.example.playlistmaker.search.model.domain.Track

interface HistoryRepository {
    fun addTrackToHistory(track: Track)
    fun getTracksHistory(): List<Track>
    fun clearHistory():Boolean
}