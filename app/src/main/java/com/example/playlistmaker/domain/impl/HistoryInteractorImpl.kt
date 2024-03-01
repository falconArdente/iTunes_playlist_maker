package com.example.playlistmaker.domain.impl

import android.content.Context
import com.example.playlistmaker.data.impl.HistoryRepositoryImpl
import com.example.playlistmaker.data.repository.HistoryRepository
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.models.Track

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