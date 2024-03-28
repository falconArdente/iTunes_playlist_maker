package com.example.playlistmaker.settings.model.data

import com.example.playlistmaker.settings.model.data.repository.ThemeStateRepositorySharedPreferenceBasedImpl
import com.example.playlistmaker.settings.model.domain.ThemeState
import com.example.playlistmaker.settings.model.domain.ThemeStateRepository
import com.example.playlistmaker.settings.model.domain.ThemeSwitchInteractor
import com.example.playlistmaker.settings.model.domain.TurnUIAppearanceUseCase

class ThemeSwitcherInteractorImpl(
    private val repository: ThemeStateRepository,
    private val themeSwitcher: TurnUIAppearanceUseCase
) : ThemeSwitchInteractor {
    override fun getTheme(): ThemeState {
        return repository.getThemeStateFromRepo()
    }

    override fun turnThemeTo(themeTurnOnTo: ThemeState) {
        themeSwitcher.turnThemeTo(themeTurnOnTo)
        if (repository is ThemeStateRepositorySharedPreferenceBasedImpl) {
            repository.saveThemeStateToRepo(themeTurnOnTo)
        }
    }
}