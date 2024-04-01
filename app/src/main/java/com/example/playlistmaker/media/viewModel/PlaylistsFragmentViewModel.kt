package com.example.playlistmaker.media.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.media.model.domain.Playlist
import com.example.playlistmaker.media.model.domain.PlaylistsInteractor

class PlaylistsFragmentViewModel(playlistsInteractor: PlaylistsInteractor) :
    ViewModel() {
    private var playlists: MutableLiveData<List<Playlist>> = MutableLiveData(listOf())

    init {
        playlists.value = playlistsInteractor.providePlaylists().toList()
    }

    fun observePlaylists(): LiveData<List<Playlist>> = playlists
}

