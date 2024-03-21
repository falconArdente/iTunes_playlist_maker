package com.example.playlistmaker.player.model.domain

import com.example.playlistmaker.search.model.domain.Track

interface Player {
    fun play()
    fun pause()
    fun stop()
    fun setTrack(trackToPlay: Track)
    fun getCurrentState(): PlayState
    fun getCurrentPosition(): Int
    fun destroy()
    fun setConsumer(playEventsConsumer: MusicPlayInteractor.MusicPlayEventsConsumer)
}