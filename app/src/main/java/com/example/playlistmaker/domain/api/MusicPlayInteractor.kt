package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface MusicPlayInteractor {
    fun play()
    fun pause()
    fun stop()
    fun setTrack(trackToPlay: Track)
    fun getCurrentPosition(): Int
    fun getCurrentState(): PlayState
    interface MusicPlayEventsConsumer {
        fun playEventConsume()
        fun pauseEventConsume()
        fun readyToPlayEventConsume()
    }
}