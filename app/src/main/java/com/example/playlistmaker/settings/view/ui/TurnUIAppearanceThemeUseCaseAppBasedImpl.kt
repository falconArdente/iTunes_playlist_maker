package com.example.playlistmaker.settings.view.ui

import android.app.Application
import com.example.playlistmaker.App
import com.example.playlistmaker.settings.model.domain.ThemeState
import com.example.playlistmaker.settings.model.domain.TurnUIAppearanceUseCase

class TurnUIAppearanceThemeUseCaseAppBasedImpl(val application: Application) :
    TurnUIAppearanceUseCase {
    override fun turnThemeTo(themeTurnOnTo: ThemeState) {
        (application as App).switchTheme(themeTurnOnTo == ThemeState.NIGHT_THEME)
    }
}