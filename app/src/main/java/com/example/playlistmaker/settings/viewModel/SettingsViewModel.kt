package com.example.playlistmaker.settings.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.model.domain.ThemeState
import com.example.playlistmaker.settings.model.domain.ThemeSwitchInteractor

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }

    private val darkThemeIterator: ThemeSwitchInteractor =
        Creator.provideThemeSwitchIterator(getApplication())
    private var isDarkTheme = MutableLiveData<ThemeState>(darkThemeIterator.getTheme())

    fun getThemeSwitchState(): LiveData<ThemeState> = isDarkTheme
    fun doSwitchTheThemeState(themeState: ThemeState) {
        darkThemeIterator.turnThemeTo(themeState)
    }

    fun emailToSupport() = Creator.provideEmailToSupportUseCase(getApplication()).execute()
    fun goToAgreement() = Creator.provideGoToAgreementInfoUseCase(getApplication()).execute()
    fun doShareAnApp() = Creator.provideShareAnAppUseCase(getApplication()).execute()

}