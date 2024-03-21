package com.example.playlistmaker.settings.view.ui

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.settings.model.domain.ThemeState
import com.example.playlistmaker.settings.model.domain.TurnUIAppearanceUseCase

class TurnUIAppearanceThemeUsingDelegateDirectly():TurnUIAppearanceUseCase {
    override fun turnThemeTo(themeTurnOnTo: ThemeState) {
        AppCompatDelegate.setDefaultNightMode(
            if (themeTurnOnTo == ThemeState.NIGHT_THEME)
                AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}