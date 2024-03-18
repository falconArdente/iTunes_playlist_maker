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

class SettingsViewModel(application:Application) : AndroidViewModel(application) {
    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(this[APPLICATION_KEY] as Application)
            }
        }

        private const val THEME_SWITCH_DEBOUNCE_DELAY: Long = 750L
    }

    private var isDarkTheme = MutableLiveData<Boolean>(true)

    fun getThemeSwitchState(): LiveData<Boolean> = isDarkTheme
    fun doSwitchTheThemeState() {

    }

    fun emailToSupport() = Creator.provideEmailToSupportUseCase(getApplication()).execute()
    fun goToAgreement() = Creator.provideGoToAgreementInfoUseCase(getApplication()).execute()
    fun doShareAnApp() = Creator.provideShareAnAppUseCase().execute(getApplication())
}