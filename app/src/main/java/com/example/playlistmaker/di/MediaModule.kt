package com.example.playlistmaker.di

import com.example.playlistmaker.media.model.domain.FavoriteTracksInteractor
import com.example.playlistmaker.media.model.domain.PlaylistsInteractor
import com.example.playlistmaker.media.model.domain.repository.FavoriteTracksIteractorImpl
import com.example.playlistmaker.media.model.domain.repository.FavoriteTracksRepository
import com.example.playlistmaker.media.model.domain.repository.PlaylistsIteractorImpl
import com.example.playlistmaker.media.model.domain.repository.PlaylistsRepository
import com.example.playlistmaker.media.model.domain.repository.MokUpFavoriteTracksRepository
import com.example.playlistmaker.media.model.domain.repository.MokUpPlaylistsRepository
import com.example.playlistmaker.media.viewModel.FavoriteTracksFragmentViewModel
import com.example.playlistmaker.media.viewModel.PlaylistsFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaModule = module {
    viewModel {
        FavoriteTracksFragmentViewModel(get())
    }
    factory<FavoriteTracksInteractor> { FavoriteTracksIteractorImpl(get()) }
    factory<FavoriteTracksRepository> { MokUpFavoriteTracksRepository() }
    viewModel {
        PlaylistsFragmentViewModel(get())
    }
    factory<PlaylistsInteractor> { PlaylistsIteractorImpl(get()) }
    factory<PlaylistsRepository> { MokUpPlaylistsRepository() }
}