package com.example.playlistmaker.media.model.data

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.playlistmaker.media.model.repository.ShareTextRepository

class ShareTextRepositoryIntentBased(private val appContext: Context) : ShareTextRepository {
    override fun execute(text: String) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, text.trimIndent())
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            appContext.startActivity(shareIntent)
        } catch (e: Exception) {
            Log.e("SHARE TEXT", e.message.toString())
        }
    }
}