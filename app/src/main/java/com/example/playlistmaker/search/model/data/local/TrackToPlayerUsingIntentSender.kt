package com.example.playlistmaker.search.model.data.local

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.playlistmaker.search.model.data.repository.TrackSender
import com.example.playlistmaker.search.model.domain.Track
import com.google.gson.Gson

private const val TRACK_KEY = "track"

class TrackToPlayerUsingIntentSender(
    private val intent: Intent,
    private val gson: Gson,
    private val application: Application
) : TrackSender {

    override fun sendTrack(trackToSend: Track) {
        intent.putExtra(TRACK_KEY, gson.toJson(trackToSend))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(application, intent, null)
    }
}