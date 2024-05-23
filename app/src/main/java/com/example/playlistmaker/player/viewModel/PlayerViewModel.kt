package com.example.playlistmaker.player.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.model.domain.CurrentFavoriteTrackInteractor
import com.example.playlistmaker.player.model.domain.GetTrackToPlayUseCase
import com.example.playlistmaker.player.model.domain.MusicPlayInteractor
import com.example.playlistmaker.player.model.domain.PlayState
import com.example.playlistmaker.search.model.domain.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val player: MusicPlayInteractor,
    private val currentFavoriteInteractor: CurrentFavoriteTrackInteractor
) : ViewModel() {
    companion object {
        private const val DURATION_RENEWAL_DELAY: Long = 300L
    }

    init {
        player.setConsumer(object : MusicPlayInteractor.MusicPlayEventsConsumer {
            override fun playEventConsume() {
                playerScreenState.value =
                    getPlayerScreenState().value?.copy(playState = PlayState.Playing)
                durationUpdateJob = viewModelScope.launch {
                    while (true) {
                        playerScreenState.value =
                            getPlayerScreenState().value?.copy(currentPosition = player.getCurrentPosition())
                        delay(DURATION_RENEWAL_DELAY)
                    }
                }
            }

            override fun pauseEventConsume() {
                durationUpdateJob?.cancel()
                playerScreenState.value =
                    getPlayerScreenState().value?.copy(playState = PlayState.Paused)
            }

            override fun readyToPlayEventConsume() {
                durationUpdateJob?.cancel()
                playerScreenState.value =
                    getPlayerScreenState().value?.copy(
                        playState = PlayState.ReadyToPlay,
                        currentPosition = 0
                    )
            }
        })

    }

    private val playerScreenState = MutableLiveData(PlayerScreenState())
    private var durationUpdateJob: Job? = null
    private val isFavoriteLiveData = MutableLiveData(false)
    private var isFavoriteInteractionJob: Job? = null
    fun getIsFavorite(): LiveData<Boolean> = isFavoriteLiveData

    fun play() = player.play()
    fun pause() = player.pause()

    fun getPlayerScreenState(): LiveData<PlayerScreenState> = playerScreenState
    fun setTrackProvider(provider: GetTrackToPlayUseCase) {
        val track: Track = provider.getTrackToPlay()
        if (track != getPlayerScreenState().value?.track) {
            player.setTrack(track)
            currentFavoriteInteractor.setCurrentTrack(track)
            launchIsFavoriteDataInteraction()
            playerScreenState.postValue(
                PlayerScreenState(
                    track,
                    player.getCurrentState(),
                    player.getCurrentPosition()
                )
            )
        }
    }

    private fun launchIsFavoriteDataInteraction() {
        isFavoriteInteractionJob?.cancel()
        isFavoriteInteractionJob=viewModelScope.launch(Dispatchers.IO) {
            currentFavoriteInteractor.isTrackFavorite()
                .collect { isFavoriteLiveData.postValue(it) }
        }
    }

    fun addToFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            currentFavoriteInteractor.addTrackToFavorites()
        }
    }

    fun removeFromFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            currentFavoriteInteractor.removeTrackFromFavorites()
        }
    }

    override fun onCleared() {
        player.destroy()
        super.onCleared()
    }
}