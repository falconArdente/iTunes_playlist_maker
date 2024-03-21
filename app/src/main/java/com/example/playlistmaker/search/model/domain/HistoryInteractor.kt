package com.example.playlistmaker.search.model.domain

interface HistoryInteractor {
    fun addTrackToHistory(track: Track)
    fun getTracksHistory(): List<Track>
    fun clearHistory()
}