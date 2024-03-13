package com.example.playlistmaker.player.domain

import com.example.playlistmaker.search.domain.Track

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