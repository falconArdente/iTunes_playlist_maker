package com.example.playlistmaker

import android.util.Log
import com.google.gson.Gson

const val SEARCH_LIST_KEY = "search_list"

class SearchHistory {
    var tracks: ArrayList<Track> = arrayListOf()
    private val gson = Gson()
    fun addTrack(track: Track) {
        if (tracks.contains(track)) tracks.remove(track)
        val size = tracks.size
        if (size > 9) tracks.removeLast()
        tracks.add(0,track)
    }

    fun saveToVault() {
        App.appPreferences.edit()
            .putString(SEARCH_LIST_KEY, gson.toJson(tracks))
            .apply()
        Log.d("VAULT", "done saving")
    }

    fun getFromVault() {
        val vaultString = App.appPreferences.getString(SEARCH_LIST_KEY, null)
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