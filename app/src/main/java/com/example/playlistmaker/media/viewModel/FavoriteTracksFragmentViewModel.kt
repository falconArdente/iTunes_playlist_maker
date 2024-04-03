package com.example.playlistmaker.media.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.model.domain.Track

class FavoriteTracksFragmentViewModel :
    ViewModel() {
    private var tracks: MutableLiveData<List<Track>> = MutableLiveData(listOf())

    fun observeFavoriteTracks(): LiveData<List<Track>> = tracks
}

