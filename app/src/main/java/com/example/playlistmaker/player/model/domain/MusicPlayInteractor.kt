package com.example.playlistmaker.player.model.domain

import com.example.playlistmaker.search.model.domain.Track

interface MusicPlayInteractor {
    fun play()
    fun pause()
    fun stop()
    fun setTrack(trackToPlay: Track)
    fun destroy()
    fun getCurrentPosition(): Int
    fun getCurrentState(): PlayState
    interface MusicPlayEventsConsumer {
        fun playEventConsume()
        fun pauseEventConsume()
        fun readyToPlayEventConsume()
    }
}