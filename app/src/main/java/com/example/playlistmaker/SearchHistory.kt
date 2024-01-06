package com.example.playlistmaker

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson

const val SEARCH_LIST_KEY = "search_list"
const val HISTORY_SIZE: Int = 9

class SearchHistory(private val appPreferences: SharedPreferences) {
    val tracks: ArrayList<Track> = arrayListOf()
    private val gson = Gson()
    fun addTrack(track: Track) {
        if (tracks.contains(track)) tracks.remove(track)
        val size = tracks.size
        if (size > HISTORY_SIZE) tracks.removeLast()
        tracks.add(0, track)
        saveToVault()
    }

    private fun saveToVault() {
        appPreferences.edit()
            .putString(SEARCH_LIST_KEY, gson.toJson(tracks))
            .apply()
        Log.d("VAULT", "done saving")
    }

    fun getFromVault() {
        val vaultString = appPreferences.getString(SEARCH_LIST_KEY, null)
        if (vaultString.isNullOrEmpty()) {
            Log.d("VAULT", "is Empty")
            return
        } else {
            tracks.clear()
            tracks.addAll(
                gson.fromJson(
                    vaultString, Array<Track>::class.java
                )
            )
            Log.d("VAULT", "Done get " + tracks.size + " items")
        }
    }

    fun clear() {
        tracks.clear()
        saveToVault()
    }
}