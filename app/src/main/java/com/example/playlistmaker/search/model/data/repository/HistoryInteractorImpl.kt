package com.example.playlistmaker.search.model.data.repository

import com.example.playlistmaker.search.model.domain.HistoryInteractor
import com.example.playlistmaker.search.model.domain.Track

class HistoryInteractorImpl(private var historyRepository: HistoryRepository) : HistoryInteractor {
    override fun addTrackToHistory(track: Track) {
        historyRepository.addTrackToHistory(track)
    }

    override fun getTracksHistory(): List<Track> {
        return historyRepository.getTracksHistory()
    }

    override fun clearHistory() {
        historyRepository.clearHistory()
    }

}