package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.di.mediaModule
import com.example.playlistmaker.di.playerModule
import com.example.playlistmaker.di.searchModule
import com.example.playlistmaker.di.settingsModule
import com.example.playlistmaker.settings.model.domain.ThemeSwitchInteractor
import com.markodevcic.peko.PermissionRequester
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(settingsModule, searchModule, mediaModule, playerModule)
        }
        val themeInteractor: ThemeSwitchInteractor by inject()
        themeInteractor.turnThemeTo(themeInteractor.getTheme())
        PermissionRequester.initialize(applicationContext)
    }
}