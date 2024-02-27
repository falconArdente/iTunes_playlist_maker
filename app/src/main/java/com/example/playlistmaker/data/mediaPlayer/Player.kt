package com.example.playlistmaker.data.mediaPlayer

import com.example.playlistmaker.domain.api.MusicPlayInteractor
import com.example.playlistmaker.domain.models.Track

interface Player {

    fun play()
    fun pause()
    fun stop()
    fun setTrack(trackToPlay: Track)
    fun getCurrentState(): MusicPlayInteractor.PlayState
    fun getCurrentPosition(): Int
    fun destroy()
    fun setConsumer(playEventsConsumer: MusicPlayInteractor.MusicPlayEventsConsumer)
}