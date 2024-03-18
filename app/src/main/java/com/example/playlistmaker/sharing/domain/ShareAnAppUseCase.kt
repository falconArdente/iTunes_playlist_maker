package com.example.playlistmaker.sharing.domain

import android.app.Application

interface ShareAnAppUseCase {
    fun execute(application: Application)
}