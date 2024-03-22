package com.example.playlistmaker.di

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
import com.example.playlistmaker.settings.model.data.ThemeStateRepositorySharedPreferenceBasedImpl
import com.example.playlistmaker.settings.model.data.ThemeSwitcherInteractorImpl
import com.example.playlistmaker.settings.model.domain.ThemeStateRepository
import com.example.playlistmaker.settings.model.domain.ThemeSwitchInteractor
import com.example.playlistmaker.settings.model.domain.TurnUIAppearanceUseCase
import com.example.playlistmaker.settings.view.ui.TurnUIAppearanceThemeUsingDelegateDirectly
import com.example.playlistmaker.settings.viewModel.SettingsViewModel
import com.example.playlistmaker.sharing.data.EmailToSupportImpl
import com.example.playlistmaker.sharing.data.GoToAgreementInfoUseCaseImpl
import com.example.playlistmaker.sharing.data.ShareAnAppImpl
import com.example.playlistmaker.sharing.domain.EmailToSupportUseCase
import com.example.playlistmaker.sharing.domain.GoToAgreementInfoUseCase
import com.example.playlistmaker.sharing.domain.ShareAnAppUseCase
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    viewModel { SettingsViewModel() }
    factory<ThemeSwitchInteractor> {
        ThemeSwitcherInteractorImpl(get(), get())
    }
    single<ThemeStateRepository> { ThemeStateRepositorySharedPreferenceBasedImpl(androidApplication()) }
    factory<TurnUIAppearanceUseCase> { TurnUIAppearanceThemeUsingDelegateDirectly() }
    factory<ShareAnAppUseCase> { ShareAnAppImpl(androidApplication()) }
    factory<EmailToSupportUseCase> { EmailToSupportImpl(androidApplication()) }
    factory<GoToAgreementInfoUseCase> { GoToAgreementInfoUseCaseImpl(androidApplication()) }
}
val searchModule = module {
    viewModel { SearchViewModel() }
    single<HistoryInteractor> {
        HistoryInteractorImpl(get())
    }
    //to see on
    single<HistoryRepository> { HistoryRepositorySharedPreferenceBased(androidApplication()) }
    single<SearchInteractor> {
        SearchInteractorImpl(get())
    }
    single<SearchRepository> { SearchRepositoryImpl(get()) }
    single<NetworkClient> { RetrofitNetworkClient() }
    factory<SendTrackToPlayerUseCase> {
        SendTrackToPlayerProvider(get())
    }
    factory<TrackSender> { TrackToPlayerUsingIntentSender(androidApplication()) }
}