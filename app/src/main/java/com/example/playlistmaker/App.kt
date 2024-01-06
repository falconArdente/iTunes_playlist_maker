package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

private const val APP_PREFERENCES_FILE_NAME = "playlistMaker_shared_preference"
private const val DARK_THEME_KEY = "dark_theme_is_on"

class App : Application() {
    private var darkThemeIsOn: Boolean = false
    private lateinit var appPreferences: SharedPreferences

    companion object {
        lateinit var history: SearchHistory
    }

    override fun onCreate() {
        super.onCreate()
        appPreferences = getSharedPreferences(APP_PREFERENCES_FILE_NAME, MODE_PRIVATE)
        darkThemeIsOn = appPreferences.getBoolean(DARK_THEME_KEY, false)
        switchTheme(darkThemeIsOn)
        history = SearchHistory(appPreferences)
    }

    private fun saveDarkThemeState() {
        appPreferences.edit()
            .putBoolean(DARK_THEME_KEY, darkThemeIsOn)
            .apply()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkThemeIsOn = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        saveDarkThemeState()
    }
}