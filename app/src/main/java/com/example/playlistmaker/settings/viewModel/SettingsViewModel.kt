package com.example.playlistmaker.settings.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.model.domain.ThemeState
import com.example.playlistmaker.settings.model.domain.ThemeSwitchInteractor
import com.example.playlistmaker.sharing.domain.EmailToSupportUseCase
import com.example.playlistmaker.sharing.domain.GoToAgreementInfoUseCase
import com.example.playlistmaker.sharing.domain.ShareAnAppUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SettingsViewModel() : ViewModel() {
    object settingsKoinInjection : KoinComponent {
        val themeSwitchInteractor: ThemeSwitchInteractor by inject()
        val shareAnAppUseCase: ShareAnAppUseCase by inject()
        val emailToSupport: EmailToSupportUseCase by inject()
        val goToAgreementInfoUseCase: GoToAgreementInfoUseCase by inject()
    }

    private val darkThemeIterator: ThemeSwitchInteractor =
        settingsKoinInjection.themeSwitchInteractor
    private var isDarkTheme = MutableLiveData<ThemeState>(darkThemeIterator.getTheme())

    fun getThemeSwitchState(): LiveData<ThemeState> = isDarkTheme
    fun doSwitchTheThemeState(themeState: ThemeState) {
        settingsKoinInjection.themeSwitchInteractor.turnThemeTo(themeState)
    }

    fun emailToSupport() = settingsKoinInjection.emailToSupport.execute()
    fun goToAgreement() = settingsKoinInjection.goToAgreementInfoUseCase.execute()
    fun doShareAnApp() = settingsKoinInjection.shareAnAppUseCase.execute()

}