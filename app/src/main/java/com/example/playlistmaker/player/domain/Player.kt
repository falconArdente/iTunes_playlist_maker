package com.example.playlistmaker.player.domain

import com.example.playlistmaker.search.domain.Track

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