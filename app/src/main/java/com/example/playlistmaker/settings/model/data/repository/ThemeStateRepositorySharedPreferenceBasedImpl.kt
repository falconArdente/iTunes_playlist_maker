package com.example.playlistmaker.settings.model.data.repository

import android.app.Application
import android.content.SharedPreferences
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.model.domain.ThemeState
import com.example.playlistmaker.settings.model.domain.ThemeStateRepository

class ThemeStateRepositorySharedPreferenceBasedImpl(application: Application) :
    ThemeStateRepository {
    private val appPreferences: SharedPreferences
    private val darkThemeKey: String

    init {
        appPreferences = application.getSharedPreferences(
            application.getString(R.string.APP_PREFERENCES_FILE_NAME),
            Application.MODE_PRIVATE
        )
        darkThemeKey = application.getString(R.string.DARK_THEME_KEY)
    }

    override fun getThemeStateFromRepo(): ThemeState {
        return if (appPreferences.getBoolean(
                darkThemeKey,
                false
            )
        ) ThemeState.NIGHT_THEME else ThemeState.DAY_THEME
    }

    override fun saveThemeStateToRepo(themeStateToSave: ThemeState) {
        appPreferences.edit()
            .putBoolean(darkThemeKey, themeStateToSave == ThemeState.NIGHT_THEME)
            .apply()
    }
}