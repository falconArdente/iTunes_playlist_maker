package com.example.playlistmaker.player.viewModel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.model.domain.GetTrackToPlayUseCase
import com.example.playlistmaker.player.model.domain.MusicPlayInteractor
import com.example.playlistmaker.player.model.domain.PlayState
import com.example.playlistmaker.search.model.domain.Track

class PlayerViewModel(private val player: MusicPlayInteractor) : ViewModel() {
    companion object {
        private const val DURATION_RENEWAL_DELAY: Long = 421L
    }

    init {
        player.setConsumer(object : MusicPlayInteractor.MusicPlayEventsConsumer {
            override fun playEventConsume() {
                playerScreenState.value =
                    getPlayerScreenState().value?.copy(playState = PlayState.Playing)
                startDurationUpdate.run()
            }

            override fun pauseEventConsume() {
                handler.removeCallbacks(startDurationUpdate)
                playerScreenState.value =
                    getPlayerScreenState().value?.copy(playState = PlayState.Paused)
            }

            override fun readyToPlayEventConsume() {
                handler.removeCallbacks(startDurationUpdate)
                playerScreenState.value =
                    getPlayerScreenState().value?.copy(
                        playState = PlayState.ReadyToPlay,
                        currentPosition = 0
                    )
            }
        })
    }

    val playerScreenState = MutableLiveData(PlayerScreenState())
    private val handler = Handler(Looper.getMainLooper())

    val startDurationUpdate: Runnable = object : Runnable {
        override fun run() {
            playerScreenState.value =
                getPlayerScreenState().value?.copy(currentPosition = player.getCurrentPosition())
            handler.postDelayed(this, DURATION_RENEWAL_DELAY)
        }
    }

    fun play() = player.play()
    fun pause() = player.pause()

    fun getPlayerScreenState(): LiveData<PlayerScreenState> = playerScreenState
    fun setTrackProvider(provider: GetTrackToPlayUseCase) {
        val track: Track = provider.getTrackToPlay()
        if (track != getPlayerScreenState().value?.track) {
            player.setTrack(track)
            playerScreenState.postValue(
                PlayerScreenState(
                    track,
                    player.getCurrentState(),
                    player.getCurrentPosition()
                )
            )
        }
    }

    override fun onCleared() {
        handler.removeCallbacks(startDurationUpdate)
        player.destroy()
        super.onCleared()
    }
}