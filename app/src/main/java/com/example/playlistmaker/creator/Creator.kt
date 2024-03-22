package com.example.playlistmaker.creator

import android.app.Application
import com.example.playlistmaker.settings.model.data.ThemeStateRepositorySharedPreferenceBasedImpl
import com.example.playlistmaker.settings.model.data.ThemeSwitcherInteractorImpl
import com.example.playlistmaker.settings.view.ui.TurnUIAppearanceThemeUsingDelegateDirectly

object Creator {

    fun provideThemeSwitchIterator(application: Application) =
        ThemeSwitcherInteractorImpl(
            ThemeStateRepositorySharedPreferenceBasedImpl(application),
            TurnUIAppearanceThemeUsingDelegateDirectly()
        )

}