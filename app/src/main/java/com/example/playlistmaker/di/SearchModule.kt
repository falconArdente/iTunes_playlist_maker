package com.example.playlistmaker.di

import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import com.example.playlistmaker.player.view.PlayerActivity
import com.example.playlistmaker.search.model.data.local.HistoryRepositorySharedPreferenceBased
import com.example.playlistmaker.search.model.data.local.TrackToPlayerUsingIntentSender
import com.example.playlistmaker.search.model.data.network.NetworkClient
import com.example.playlistmaker.search.model.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.model.data.network.SearchRepositoryImpl
import com.example.playlistmaker.search.model.data.repository.HistoryInteractorImpl
import com.example.playlistmaker.search.model.data.repository.HistoryRepository
import com.example.playlistmaker.search.model.data.repository.SearchInteractorImpl
import com.example.playlistmaker.search.model.data.repository.SearchRepository
import com.example.playlistmaker.search.model.data.repository.SendTrackToPlayerProvider
import com.example.playlistmaker.search.model.data.repository.TrackSender
import com.example.playlistmaker.search.model.domain.HistoryInteractor
import com.example.playlistmaker.search.model.domain.SearchInteractor
import com.example.playlistmaker.search.model.domain.SendTrackToPlayerUseCase
import com.example.playlistmaker.search.viewModel.SearchViewModel
import com.google.gson.Gson
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val APP_PREFERENCES_FILE_NAME = "playlistMaker_shared_preference"
private const val BASE_URL = "https://itunes.apple.com"

val searchModule = module {
    viewModel { SearchViewModel(get(), get(), get()) }
    factory<HistoryInteractor> {
        HistoryInteractorImpl(get())
    }
    single<HistoryRepository> { HistoryRepositorySharedPreferenceBased(get(), get()) }
    factory<SearchInteractor> {
        SearchInteractorImpl(get())
    }
    single<SearchRepository> { SearchRepositoryImpl(get(),androidApplication()) }
    factory<NetworkClient> { RetrofitNetworkClient(get()) }
    factory<SendTrackToPlayerUseCase> {
        SendTrackToPlayerProvider(get())
    }
    factory<TrackSender> {
        TrackToPlayerUsingIntentSender(get(), get(), androidApplication())
    }
    factory { Intent(androidApplication(), PlayerActivity::class.java) }
    single<SharedPreferences> {
        androidApplication().getSharedPreferences(
            APP_PREFERENCES_FILE_NAME,
            Application.MODE_PRIVATE
        )
    }
    single { Gson() }
    single<Retrofit> {
        return@single Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}