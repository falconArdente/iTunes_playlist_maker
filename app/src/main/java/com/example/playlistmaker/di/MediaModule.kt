package com.example.playlistmaker.di

import androidx.room.Room
import com.example.playlistmaker.media.model.data.SharePlaylistTextBasedImpl
import com.example.playlistmaker.media.model.data.ShareTextRepositoryIntentBased
import com.example.playlistmaker.media.model.data.db.AppDbRoomBased
import com.example.playlistmaker.media.model.data.db.FavoriteTracksRepositoryRoomImpl
import com.example.playlistmaker.media.model.data.db.PlaylistsRepositoryRoomImpl
import com.example.playlistmaker.media.model.data.db.dao.FavoriteTracksDao
import com.example.playlistmaker.media.model.data.db.dao.PlaylistsDao
import com.example.playlistmaker.media.model.data.storage.PrivateStorageImageRepositoryImpl
import com.example.playlistmaker.media.model.domain.FavoriteTracksInteractor
import com.example.playlistmaker.media.model.domain.PlaylistsInteractor
import com.example.playlistmaker.media.model.domain.SaveImageToStorageUseCase
import com.example.playlistmaker.media.model.domain.SelectAnImageUseCase
import com.example.playlistmaker.media.model.domain.SharePlaylistUseCase
import com.example.playlistmaker.media.model.repository.FavoriteTracksInteractorImpl
import com.example.playlistmaker.media.model.repository.FavoriteTracksRepository
import com.example.playlistmaker.media.model.repository.ImageSelectionRepository
import com.example.playlistmaker.media.model.repository.PlaylistsInteractorImpl
import com.example.playlistmaker.media.model.repository.PlaylistsRepository
import com.example.playlistmaker.media.model.repository.SaveImageToStorageUseCaseImpl
import com.example.playlistmaker.media.model.repository.SelectAnImageUseCasePickerCompatibleImpl
import com.example.playlistmaker.media.model.repository.ShareTextRepository
import com.example.playlistmaker.media.view.ui.ImageSelectionRepositoryPhotoPickerBased
import com.example.playlistmaker.media.viewModel.CreatePlaylistViewModel
import com.example.playlistmaker.media.viewModel.EditPlaylistViewModel
import com.example.playlistmaker.media.viewModel.FavoriteTracksFragmentViewModel
import com.example.playlistmaker.media.viewModel.PlaylistItemViewModel
import com.example.playlistmaker.media.viewModel.PlaylistsFragmentViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private const val DATABASE_NAME = "tracks_database.db"
val mediaModule = module {
    viewModel {
        FavoriteTracksFragmentViewModel(
            favoriteTracksInteractor = get(), trackToPlayerUseCase = get()
        )
    }
    viewModel {
        PlaylistsFragmentViewModel(get())
    }
    viewModel {
        CreatePlaylistViewModel(
            imageSelector = get(),
            saverForImage = get(),
            dataTable = get(),
            androidContext = androidContext()
        )
    }
    viewModel {
        PlaylistItemViewModel(
            dataSource = get(),
            trackToPlayerUseCase = get(),
            sharePlaylistUseCase = get()
        )
    }
    viewModel {
        EditPlaylistViewModel(
            imageSelector = get(),
            saverForImage = get(),
            dataTable = get(),
            androidContext = androidContext()
        )
    }
    single<AppDbRoomBased> {
        Room.databaseBuilder(androidContext(), AppDbRoomBased::class.java, DATABASE_NAME).build()
    }
    single<FavoriteTracksDao> {
        get<AppDbRoomBased>().favoriteTracksDao()
    }
    single<PlaylistsDao> {
        get<AppDbRoomBased>().playlistsDao()
    }
    factory<FavoriteTracksRepository> {
        FavoriteTracksRepositoryRoomImpl(favoritesTable = get())
    }
    factory<FavoriteTracksInteractor> {
        FavoriteTracksInteractorImpl(repository = get())
    }
    factory<PlaylistsRepository> {
        PlaylistsRepositoryRoomImpl(playlistsTable = get())
    }
    factory<PlaylistsInteractor> {
        PlaylistsInteractorImpl(repository = get())
    }
    factory<SelectAnImageUseCase> {
        SelectAnImageUseCasePickerCompatibleImpl(repository = get())
    }
    factory<ImageSelectionRepository> {
        ImageSelectionRepositoryPhotoPickerBased()
    }
    factory<SaveImageToStorageUseCase> {
        SaveImageToStorageUseCaseImpl(
            repository = PrivateStorageImageRepositoryImpl(androidContext())
        )
    }
    factory<SharePlaylistUseCase> {
        SharePlaylistTextBasedImpl(
            repository = get(),
            appContext = androidContext()
        )
    }
    factory<ShareTextRepository> {
        ShareTextRepositoryIntentBased(androidContext())
    }
}