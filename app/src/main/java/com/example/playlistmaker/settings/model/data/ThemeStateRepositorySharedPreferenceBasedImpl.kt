package com.example.playlistmaker.settings.model.data

import android.app.Application
import android.content.SharedPreferences
import com.example.playlistmaker.settings.model.domain.ThemeState
import com.example.playlistmaker.settings.model.domain.ThemeStateRepository

private const val APP_PREFERENCES_FILE_NAME = "playlistMaker_shared_preference"
private const val DARK_THEME_KEY = "dark_theme_is_on"

class ThemeStateRepositorySharedPreferenceBasedImpl(val application: Application) :
    ThemeStateRepository {
    private val appPreferences: SharedPreferences =
        application.getSharedPreferences(APP_PREFERENCES_FILE_NAME, Application.MODE_PRIVATE)

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