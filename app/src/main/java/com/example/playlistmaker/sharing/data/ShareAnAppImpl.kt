package com.example.playlistmaker.sharing.data

import android.app.Application
import android.content.Intent
import android.util.Log
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.ShareAnAppUseCase

class ShareAnAppImpl(val application: Application) : ShareAnAppUseCase {
    override fun execute() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, application.getString(R.string.app_name))
            val shareMessage = "\n${application.getString(R.string.to_share_text)}\n\n" +
                    application.getString(R.string.playmarket_URL_base) + application.applicationInfo.uid
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage.trimIndent())
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            application.startActivity(shareIntent)
        } catch (e: Exception) {
            Log.e("SHARE", e.message.toString())
        }
    }
}