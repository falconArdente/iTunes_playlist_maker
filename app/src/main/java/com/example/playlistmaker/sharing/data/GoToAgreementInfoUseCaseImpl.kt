package com.example.playlistmaker.sharing.data

import android.app.Application
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.GoToAgreementInfoUseCase

class GoToAgreementInfoUseCaseImpl(val application: Application) : GoToAgreementInfoUseCase {
    override fun execute() {
        val url: String = application.getString(R.string.agreement_URL)
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(browserIntent)
    }
}