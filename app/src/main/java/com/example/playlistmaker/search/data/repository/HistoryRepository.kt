package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.search.domain.Track

interface HistoryRepository {
    fun addTrackToHistory(track: Track)
    fun getTracksHistory(): List<Track>
    fun clearHistory():Boolean
}