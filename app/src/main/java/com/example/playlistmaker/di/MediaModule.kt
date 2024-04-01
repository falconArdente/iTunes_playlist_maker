package com.example.playlistmaker.di

import com.example.playlistmaker.media.model.domain.FavoriteTracksInteractor
import com.example.playlistmaker.media.model.domain.repository.FavoriteTracksIteractorImpl
import com.example.playlistmaker.media.model.domain.repository.FavoriteTracksRepository
import com.example.playlistmaker.media.model.domain.repository.mokUpFavoriteTracksRepository
import com.example.playlistmaker.media.viewModel.FavoriteTracksFragmentViewModel
import com.google.gson.Gson
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private const val APP_PREFERENCES_FILE_NAME = "playlistMaker_shared_preference"


val mediaModule = module {
    viewModel {
        FavoriteTracksFragmentViewModel(get())
    }
factory<FavoriteTracksInteractor> { FavoriteTracksIteractorImpl(get()) }
    factory<FavoriteTracksRepository> { mokUpFavoriteTracksRepository() }
    single { Gson() }
}