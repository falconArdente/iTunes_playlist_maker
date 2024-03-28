package com.example.playlistmaker.di

import android.app.Activity
import android.media.MediaPlayer
import com.example.playlistmaker.player.model.controller.MusicPlayerInteractorImpl
import com.example.playlistmaker.player.model.data.MediaPlayerBasedPlayer
import com.example.playlistmaker.player.model.domain.GetTrackToPlayUseCase
import com.example.playlistmaker.player.model.domain.MusicPlayInteractor
import com.example.playlistmaker.player.model.domain.Player
import com.example.playlistmaker.player.model.repository.GetTrackToPlayUseCaseImpl
import com.example.playlistmaker.player.view.ui.TrackFromIntentRepository
import com.example.playlistmaker.player.viewModel.PlayerViewModel
import com.google.gson.Gson
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerModule = module {
    viewModel { PlayerViewModel(get()) }
    factory<MusicPlayInteractor> {
        MusicPlayerInteractorImpl(get())
    }
    factory<Player> { MediaPlayerBasedPlayer(get()) }
    factory { MediaPlayer() }
    factory<GetTrackToPlayUseCase> { (activity: Activity) ->
        GetTrackToPlayUseCaseImpl(TrackFromIntentRepository(activityToGetFrom = activity, get()))
    }
    single { Gson() }
}