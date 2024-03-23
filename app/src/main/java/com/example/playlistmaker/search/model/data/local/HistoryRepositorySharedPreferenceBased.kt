package com.example.playlistmaker.search.model.data.local

import android.app.Application
import android.content.SharedPreferences
import com.example.playlistmaker.R
import com.example.playlistmaker.search.model.data.repository.HistoryRepository
import com.example.playlistmaker.search.model.domain.Track
import com.google.gson.Gson

class HistoryRepositorySharedPreferenceBased(application: Application) :
    HistoryRepository {
    private val appPreferences: SharedPreferences
    private val searchListKey: String
    private val historySize: Int

    init {
        appPreferences = application.getSharedPreferences(
            application.getString(R.string.APP_PREFERENCES_FILE_NAME),
            Application.MODE_PRIVATE
        )
        searchListKey = application.getString(R.string.SEARCH_LIST_KEY)
        historySize = try {
            application.getString(R.string.HISTORY_SIZE).toInt()
        } catch (e: Throwable) {
            9
        }
    }

    private val tracks: ArrayList<Track> = arrayListOf()
    private val gson = Gson()
    override fun addTrackToHistory(track: Track) {
        if (tracks.contains(track)) tracks.remove(track)
        val size = tracks.size
        if (size > historySize) tracks.removeLast()
        tracks.add(0, track)
        saveToVault()
    }

    override fun getTracksHistory(): List<Track> {
        val vaultString = appPreferences.getString(searchListKey, null)
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
            .putString(searchListKey, gson.toJson(tracks))
            .apply()
    }

    override fun clearHistory(): Boolean {
        tracks.clear()
        saveToVault()
        return true
    }
}