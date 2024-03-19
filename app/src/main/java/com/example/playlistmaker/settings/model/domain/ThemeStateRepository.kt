package com.example.playlistmaker.settings.model.domain

interface ThemeStateRepository {
    fun getThemeStateFromRepo(): ThemeState
    fun saveThemeStateToRepo(themeStateToSave: ThemeState)
}