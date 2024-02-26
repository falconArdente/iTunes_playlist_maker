package com.example.playlistmaker.data.impl

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.playlistmaker.data.repository.HistoryRepository
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson

const val SEARCH_LIST_KEY = "search_list"
const val HISTORY_SIZE: Int = 9
private const val APP_PREFERENCES_FILE_NAME = "playlistMaker_shared_preference"

class HistoryRepositoryImpl(context: Context) : HistoryRepository {
    private var appPreferences: SharedPreferences =
      context.getSharedPreferences(APP_PREFERENCES_FILE_NAME, Application.MODE_PRIVATE)

    private val tracks: ArrayList<Track> = arrayListOf()
    private val gson = Gson()
    override fun addTrackToHistory(track: Track) {
        if (tracks.contains(track)) tracks.remove(track)
        val size = tracks.size
        if (size > HISTORY_SIZE) tracks.removeLast()
        tracks.add(0, track)
        saveToVault()
    }

    override fun getTracksHistory(): List<Track> {
        Log.d("history","try getTracksHistory")
        val vaultString = appPreferences.getString(SEARCH_LIST_KEY, null)
        return if (vaultString.isNullOrEmpty()) {
            Log.d("history", "is Empty")
            emptyList()
        } else {
            tracks.clear()
            tracks.addAll(
                gson.fromJson(
                    vaultString, Array<Track>::class.java
                )
            )
            Log.d("history", "Done get " + tracks.size + " items")
            tracks.toList()
        }
    }

    private fun saveToVault() {
        appPreferences.edit()
            .putString(SEARCH_LIST_KEY, gson.toJson(tracks))
            .apply()
        Log.d("history", "done saving")
    }

    override fun clearHistory():Boolean {
        tracks.clear()
        saveToVault()
        Log.d("history", "cleared")
        return true
    }
}