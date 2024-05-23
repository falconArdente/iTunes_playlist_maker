package com.example.playlistmaker.di

import androidx.room.Room
import com.example.playlistmaker.media.model.data.db.AppDbRoomBased
import com.example.playlistmaker.media.model.data.db.FavoriteTracksRepositoryRoomImpl
import com.example.playlistmaker.media.model.data.db.dao.FavoriteTracksDao
import com.example.playlistmaker.media.model.domain.FavoriteTracksInteractor
import com.example.playlistmaker.media.model.repository.FavoriteTracksInteractorImpl
import com.example.playlistmaker.media.model.repository.FavoriteTracksRepository
import com.example.playlistmaker.media.viewModel.FavoriteTracksFragmentViewModel
import com.example.playlistmaker.media.viewModel.PlaylistsFragmentViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private const val DATABASE_NAME = "tracks_database.db"
val mediaModule = module {
    viewModel {
        FavoriteTracksFragmentViewModel()
    }
    viewModel {
        PlaylistsFragmentViewModel()
    }
    single<AppDbRoomBased> {
        Room.databaseBuilder(androidContext(), AppDbRoomBased::class.java, DATABASE_NAME)
            .build()
    }
    single<FavoriteTracksDao> {
        get<AppDbRoomBased>().favoriteTracksDao()
    }
    factory<FavoriteTracksRepository> {
        FavoriteTracksRepositoryRoomImpl(get())
    }
    factory<FavoriteTracksInteractor> {
        FavoriteTracksInteractorImpl(get())
    }

}