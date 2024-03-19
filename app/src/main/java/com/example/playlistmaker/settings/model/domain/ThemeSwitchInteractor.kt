package com.example.playlistmaker.settings.model.domain

interface ThemeSwitchInteractor {
    fun getTheme(): ThemeState
    fun turnThemeTo(themeTurnOnTo: ThemeState)
}