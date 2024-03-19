package com.example.playlistmaker.search.model.data.local

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.player.view.PlayerActivity
import com.example.playlistmaker.search.model.data.repository.TrackSender
import com.example.playlistmaker.search.model.domain.Track
import com.google.gson.Gson

class TrackToPlayerUsingIntentSender(val application: Application) : TrackSender {
    private val trackKeyConst = application.getString(R.string.TRACK_KEY)
    override fun sendTrack(trackToSend: Track) {
        val intent =
            Intent(application, PlayerActivity::class.java)
        val json = Gson()
        intent.putExtra(trackKeyConst, json.toJson(trackToSend))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(application, intent, null)
    }
}