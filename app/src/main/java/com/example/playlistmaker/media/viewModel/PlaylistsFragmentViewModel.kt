package com.example.playlistmaker.media.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.media.model.domain.Playlist

class PlaylistsFragmentViewModel :
    ViewModel() {
    private var playlists: MutableLiveData<List<Playlist>> = MutableLiveData(listOf())

    fun observePlaylists(): LiveData<List<Playlist>> = playlists
}

