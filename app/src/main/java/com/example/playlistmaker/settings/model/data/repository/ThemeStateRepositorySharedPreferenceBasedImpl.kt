package com.example.playlistmaker.settings.model.data.repository

import android.content.SharedPreferences
import com.example.playlistmaker.settings.model.domain.ThemeState
import com.example.playlistmaker.settings.model.domain.ThemeStateRepository

private const val DARK_THEME_KEY = "dark_theme_is_on"

class ThemeStateRepositorySharedPreferenceBasedImpl(private val appPreferences: SharedPreferences) :
    ThemeStateRepository {

    override fun getThemeStateFromRepo(): ThemeState {
        return if (appPreferences.getBoolean(
                DARK_THEME_KEY,
                false
            )
        ) ThemeState.NIGHT_THEME else ThemeState.DAY_THEME
    }

    override fun saveThemeStateToRepo(themeStateToSave: ThemeState) {
        appPreferences.edit()
            .putBoolean(DARK_THEME_KEY, themeStateToSave == ThemeState.NIGHT_THEME)
            .apply()
    }
}