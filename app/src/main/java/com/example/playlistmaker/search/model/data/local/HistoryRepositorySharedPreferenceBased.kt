package com.example.playlistmaker.search.model.data.local

import android.content.SharedPreferences
import com.example.playlistmaker.search.model.data.repository.HistoryRepository
import com.example.playlistmaker.search.model.domain.Track
import com.google.gson.Gson

private const val SEARCH_LIST_KEY = "search_list"
private const val HISTORY_SIZE: Int = 10

class HistoryRepositorySharedPreferenceBased(
    private val appPreferences: SharedPreferences,
    private val gson: Gson
) : HistoryRepository {
    private val tracks: ArrayList<Track> = arrayListOf()
    override fun addTrackToHistory(track: Track) {
        if (tracks.contains(track)) tracks.remove(track)
        val size = tracks.size
        if (size >= HISTORY_SIZE) tracks.removeLast()
        tracks.add(0, track)
        saveToVault()
    }

    override fun getTracksHistory(): List<Track> {
        val vaultString = appPreferences.getString(SEARCH_LIST_KEY, null)
        return if (vaultString.isNullOrEmpty()) {
            emptyList()
        } else {
            tracks.clear()
            tracks.addAll(
                gson.fromJson(
                    vaultString, Array<Track>::class.java
                )
            )
            tracks.toList()
        }
    }

    private fun saveToVault() {
        appPreferences.edit()
            .putString(SEARCH_LIST_KEY, gson.toJson(tracks))
            .apply()
    }

    override fun clearHistory(): Boolean {
        tracks.clear()
        saveToVault()
        return true
    }
}