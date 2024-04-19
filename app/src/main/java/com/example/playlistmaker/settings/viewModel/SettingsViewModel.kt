package com.example.playlistmaker.settings.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.model.domain.ThemeState
import com.example.playlistmaker.settings.model.domain.ThemeSwitchInteractor
import com.example.playlistmaker.sharing.domain.EmailToSupportUseCase
import com.example.playlistmaker.sharing.domain.GoToAgreementInfoUseCase
import com.example.playlistmaker.sharing.domain.ShareAnAppUseCase

class SettingsViewModel(
    private val themeSwitchInteractor: ThemeSwitchInteractor,
    private val shareAnAppUseCase: ShareAnAppUseCase,
    private val emailToSupport: EmailToSupportUseCase,
    private val goToAgreementInfoUseCase: GoToAgreementInfoUseCase
) : ViewModel() {

    private var isDarkThemeLiveData = MutableLiveData<ThemeState>(themeSwitchInteractor.getTheme())
    fun getThemeSwitchState(): LiveData<ThemeState> = isDarkThemeLiveData
    fun doSwitchTheThemeState(themeState: ThemeState) {
        themeSwitchInteractor.turnThemeTo(themeState)
        isDarkThemeLiveData.value=themeSwitchInteractor.getTheme()
    }

    fun emailToSupport() = emailToSupport.execute()
    fun goToAgreement() = goToAgreementInfoUseCase.execute()
    fun doShareAnApp() = shareAnAppUseCase.execute()

}