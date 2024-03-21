package com.example.playlistmaker.sharing.data

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.EmailToSupportUseCase

class EmailToSupportImpl(val application: Application) : EmailToSupportUseCase {
    override fun execute() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/html"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(application.getString(R.string.my_email)))
        intent.putExtra(
            Intent.EXTRA_SUBJECT,
            application.getString(R.string.email_subject_for_support)
        )
        intent.putExtra(Intent.EXTRA_TEXT, application.getString(R.string.email_body_for_support))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(application, intent, null)
    }
}