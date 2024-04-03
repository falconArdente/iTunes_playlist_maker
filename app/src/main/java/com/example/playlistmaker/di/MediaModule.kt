package com.example.playlistmaker.di

import com.example.playlistmaker.media.viewModel.FavoriteTracksFragmentViewModel
import com.example.playlistmaker.media.viewModel.PlaylistsFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaModule = module {
    viewModel {
        FavoriteTracksFragmentViewModel()
    }
    viewModel {
        PlaylistsFragmentViewModel()
    }
}