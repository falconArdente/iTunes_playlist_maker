package com.example.playlistmaker.media.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.model.domain.FavoriteTracksInteractor
import com.example.playlistmaker.search.model.domain.SendTrackToPlayerUseCase
import com.example.playlistmaker.search.model.domain.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteTracksFragmentViewModel(
    private val favoriteTracksInteractor: FavoriteTracksInteractor,
    private val trackToPlayer: SendTrackToPlayerUseCase
) :
    ViewModel() {
    private val screenStateLiveData =
        MutableLiveData<FavoriteTracksScreenState>(FavoriteTracksScreenState.NoTracks)

    init {
        launchDbDataIncome()
    }

    fun launchDbDataIncome() {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteTracksInteractor
                .getAllTracks()
                .collect { tracks ->
                    screenStateLiveData.postValue(
                        if (tracks.isEmpty()) FavoriteTracksScreenState.NoTracks
                        else FavoriteTracksScreenState.HaveTracks(tracks)
                    )
                    Log.d("data111", "VIEWmodel")
                }
        }
    }

    var screenState: LiveData<FavoriteTracksScreenState> = screenStateLiveData
    fun openTrack(trackToOpen: Track) {
        trackToPlayer.sendToPlayer(trackToOpen)
    }
}

