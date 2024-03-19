package com.example.playlistmaker.creator

import android.app.Activity
import android.app.Application
import com.example.playlistmaker.player.model.controller.MusicPlayerInteractorImpl
import com.example.playlistmaker.player.model.data.MediaPlayerBasedPlayer
import com.example.playlistmaker.player.model.domain.GetTrackToPlayUseCase
import com.example.playlistmaker.player.model.domain.MusicPlayInteractor
import com.example.playlistmaker.player.model.repository.GetTrackToPlayUseCaseImpl
import com.example.playlistmaker.player.view.ui.TrackFromIntentRepository
import com.example.playlistmaker.search.model.data.local.HistoryRepositorySharedPreferenceBased
import com.example.playlistmaker.search.model.data.local.TrackToPlayerUsingIntentSender
import com.example.playlistmaker.search.model.data.network.RetrofitNetworlClient
import com.example.playlistmaker.search.model.data.network.SearchRepositoryImpl
import com.example.playlistmaker.search.model.data.repository.HistoryInteractorImpl
import com.example.playlistmaker.search.model.data.repository.SearchInteractorImpl
import com.example.playlistmaker.search.model.data.repository.SearchRepository
import com.example.playlistmaker.search.model.data.repository.SendTrackToPlayerProvider
import com.example.playlistmaker.search.model.domain.HistoryInteractor
import com.example.playlistmaker.search.model.domain.SearchInteractor
import com.example.playlistmaker.search.model.domain.SendTrackToPlayerUseCase
import com.example.playlistmaker.settings.model.data.ThemeStateRepositorySharedPreferenceBasedImpl
import com.example.playlistmaker.settings.model.data.ThemeSwitcherInteractorImpl
import com.example.playlistmaker.settings.view.ui.TurnUIAppearanceThemeUseCaseAppBasedImpl
import com.example.playlistmaker.sharing.data.EmailToSupportImpl
import com.example.playlistmaker.sharing.data.GoToAgreementInfoUseCaseImpl
import com.example.playlistmaker.sharing.data.ShareAnAppImpl

object Creator {
    private fun getSearchRepository(): SearchRepository {
        return SearchRepositoryImpl(RetrofitNetworlClient())
    }

    fun provideSearchInteractor(): SearchInteractor {
        return SearchInteractorImpl(getSearchRepository())
    }

    fun provideHistoryInteractor(application: Application): HistoryInteractor =
        HistoryInteractorImpl(HistoryRepositorySharedPreferenceBased(application))

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
            TurnUIAppearanceThemeUseCaseAppBasedImpl(application)
        )

    fun provideOpenTrackUseCase(application: Application): SendTrackToPlayerUseCase =
        SendTrackToPlayerProvider(TrackToPlayerUsingIntentSender(application))

    fun provideShareAnAppUseCase(application: Application) = ShareAnAppImpl(application)
    fun provideEmailToSupportUseCase(application: Application) = EmailToSupportImpl(application)
    fun provideGoToAgreementInfoUseCase(application: Application) =
        GoToAgreementInfoUseCaseImpl(application)
}