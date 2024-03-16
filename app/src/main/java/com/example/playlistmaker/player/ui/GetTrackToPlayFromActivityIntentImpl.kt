package com.example.playlistmaker.player.ui

import android.app.Activity
import com.example.playlistmaker.player.domain.GetTrackToPlayUseCase
import com.example.playlistmaker.search.domain.Track
import com.google.gson.Gson
private const val TRACK_KEY = "track"
class GetTrackToPlayFromActivityIntentImpl(val context: Activity) : GetTrackToPlayUseCase {
    override fun getTrackToPlay(): Track {
        val json = Gson()
        val tempString = context.intent.getStringExtra(TRACK_KEY)
        return if (!tempString.isNullOrEmpty()) {
            json.fromJson(tempString, Track::class.java)
        } else {
            Track(
                id = "-1", trackTitle = "Intent income error", "", "", "",
                "", "", "", "", ""
            )
        }
    }

}