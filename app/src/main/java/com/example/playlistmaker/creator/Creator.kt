package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.search.data.network.RetrofitNetworlClient
import com.example.playlistmaker.search.data.network.SearchRepositoryImpl
import com.example.playlistmaker.player.data.MediaPlayerBasedImpl
import com.example.playlistmaker.search.data.repository.SearchRepository
import com.example.playlistmaker.search.domain.HistoryInteractor
import com.example.playlistmaker.player.domain.MusicPlayInteractor
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.data.repository.HistoryInteractorImpl
import com.example.playlistmaker.player.data.MusicPlayerInteractorImpl
import com.example.playlistmaker.search.data.repository.SearchInteractorImpl

object Creator {
    private fun getSearchRepository(): SearchRepository {
        return SearchRepositoryImpl(RetrofitNetworlClient())
    }

    fun provideSearchInteractor(): SearchInteractor {
        return SearchInteractorImpl(getSearchRepository())
    }

    fun provideHistoryInteractor(context: Context): HistoryInteractor {
        return HistoryInteractorImpl(context)
    }

    fun provideMusicPlayerInteractor(musicPlayEventsConsumer: MusicPlayInteractor.MusicPlayEventsConsumer): MusicPlayInteractor {
        return MusicPlayerInteractorImpl(
            player = MediaPlayerBasedImpl(),
            musicPlayEventsConsumer = musicPlayEventsConsumer
        )
    }

}