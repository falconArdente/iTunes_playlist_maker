package com.example.playlistmaker.settings.domain

interface ThemeSwitchInteractor {
    fun getIsDarkNow():Boolean
    fun turnToDarkTheme(toDarkOne:Boolean)
}