package com.example.playlistmaker.search.domain

interface HistoryInteractor {
    fun addTrackToHistory(track: Track)
    fun getTracksHistory(): List<Track>
    fun clearHistory()
}