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
import com.example.playlistmaker.settings.domain.ThemeSwitchInteractor

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
    private var isDarkTheme = MutableLiveData<Boolean>(darkThemeIterator.getIsDarkNow())

    fun getThemeSwitchState(): LiveData<Boolean> = isDarkTheme
    fun doSwitchTheThemeState(toDarkOne: Boolean) {
        darkThemeIterator.turnToDarkTheme(toDarkOne)
    }

    fun emailToSupport() = Creator.provideEmailToSupportUseCase(getApplication()).execute()
    fun goToAgreement() = Creator.provideGoToAgreementInfoUseCase(getApplication()).execute()
    fun doShareAnApp() = Creator.provideShareAnAppUseCase().execute(getApplication())

}