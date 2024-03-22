package com.example.playlistmaker.creator

import android.app.Activity
import android.app.Application
import com.example.playlistmaker.player.model.controller.MusicPlayerInteractorImpl
import com.example.playlistmaker.player.model.data.MediaPlayerBasedPlayer
import com.example.playlistmaker.player.model.domain.GetTrackToPlayUseCase
import com.example.playlistmaker.player.model.domain.MusicPlayInteractor
import com.example.playlistmaker.player.model.repository.GetTrackToPlayUseCaseImpl
import com.example.playlistmaker.player.view.ui.TrackFromIntentRepository
import com.example.playlistmaker.settings.model.data.ThemeStateRepositorySharedPreferenceBasedImpl
import com.example.playlistmaker.settings.model.data.ThemeSwitcherInteractorImpl
import com.example.playlistmaker.settings.view.ui.TurnUIAppearanceThemeUsingDelegateDirectly

object Creator {
     fun provideMusicPlayerInteractor(musicPlayEventsConsumer: MusicPlayInteractor.MusicPlayEventsConsumer): MusicPlayInteractor {
        return MusicPlayerInteractorImpl(
            player = MediaPlayerBasedPlayer(),
            musicPlayEventsConsumer = musicPlayEventsConsumer
        )
    }

    fun provideTrackToPlayUseCase(activity: Activity): GetTrackToPlayUseCase {
        return GetTrackToPlayUseCaseImpl(TrackFromIntentRepository(activityToGetFrom = activity))
    }

    fun provideThemeSwitchIterator(application: Application) =
        ThemeSwitcherInteractorImpl(
            ThemeStateRepositorySharedPreferenceBasedImpl(application),
            TurnUIAppearanceThemeUsingDelegateDirectly()
        )

}