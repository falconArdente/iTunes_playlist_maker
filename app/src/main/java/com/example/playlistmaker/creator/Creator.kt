package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.data.impl.RetrofitNetworlClient
import com.example.playlistmaker.data.impl.SearchRepositoryImpl
import com.example.playlistmaker.data.repository.SearchRepository
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.api.SearchInteractor
import com.example.playlistmaker.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker.domain.impl.SearchInteractorImpl

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

}