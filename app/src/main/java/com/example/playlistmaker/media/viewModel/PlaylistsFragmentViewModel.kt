package com.example.playlistmaker.media.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.model.domain.PlaylistsInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistsFragmentViewModel(private val playlistSource: PlaylistsInteractor) :
    ViewModel() {
    private var mutableScreenState: MutableLiveData<PlaylistScreenState> =
        MutableLiveData(PlaylistScreenState.Empty)
    val screenStateToObserve: LiveData<PlaylistScreenState> = mutableScreenState

    init {
        runDataIntercourse()
    }

    private fun runDataIntercourse() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistSource
                .getAllPlaylists()
                .collect { listOfPlaylists ->
                    if (listOfPlaylists.isEmpty()) mutableScreenState.postValue(PlaylistScreenState.Empty)
                    else mutableScreenState.postValue(PlaylistScreenState.HaveData(listOfPlaylists))
                }
        }
    }
}

