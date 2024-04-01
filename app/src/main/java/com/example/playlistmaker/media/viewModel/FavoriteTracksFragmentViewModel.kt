package com.example.playlistmaker.media.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.media.model.domain.FavoriteTracksInteractor
import com.example.playlistmaker.search.model.domain.Track

class FavoriteTracksFragmentViewModel(favoriteTracksInteractor: FavoriteTracksInteractor) :
    ViewModel() {
    private var tracks: MutableLiveData<List<Track>> = MutableLiveData(listOf<Track>())

    init {
        tracks.value = favoriteTracksInteractor.provideTracks().toList()
    }

    fun observeFavoriteTracks(): LiveData<List<Track>> = tracks
}

