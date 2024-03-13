package com.example.playlistmaker.search.data.repository

import android.content.Context
import com.example.playlistmaker.search.data.local.HistoryRepositoryImpl
import com.example.playlistmaker.search.domain.HistoryInteractor
import com.example.playlistmaker.search.domain.Track

class HistoryInteractorImpl(context: Context) : HistoryInteractor {
    private var repo: HistoryRepository

    init {
        repo = HistoryRepositoryImpl(context)
    }

    override fun addTrackToHistory(track: Track) {
        repo.addTrackToHistory(track)
    }

    override fun getTracksHistory(): List<Track> {
        return repo.getTracksHistory()
    }

    override fun clearHistory() {
        repo.clearHistory()
    }

}