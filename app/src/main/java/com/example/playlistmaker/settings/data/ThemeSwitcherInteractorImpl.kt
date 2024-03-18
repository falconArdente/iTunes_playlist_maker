package com.example.playlistmaker.settings.data

import android.app.Application
import com.example.playlistmaker.App
import com.example.playlistmaker.settings.domain.ThemeSwitchInteractor

class ThemeSwitcherInteractorImpl(val application: Application) : ThemeSwitchInteractor {
    override fun getIsDarkNow(): Boolean = (application as App).getDarkThemeIsOnState()
    override fun turnToDarkTheme(toDarkOne: Boolean) = (application as App).switchTheme(toDarkOne)
}