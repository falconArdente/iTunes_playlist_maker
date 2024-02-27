package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.data.mediaPlayer.Player
import com.example.playlistmaker.domain.api.MusicPlayInteractor
import com.example.playlistmaker.domain.models.Track

class MusicPlayerInteractorImpl(
    private val player: Player,
    musicPlayEventsConsumer: MusicPlayInteractor.MusicPlayEventsConsumer? = null
) : MusicPlayInteractor {
    init {
        if (musicPlayEventsConsumer != null) player.setConsumer(musicPlayEventsConsumer)
    }

    override fun play() {
        player.play()
    }

    override fun pause() {
        player.pause()
    }

    override fun stop() {
        player.stop()
    }

    override fun setTrack(trackToPlay: Track) {
        player.setTrack(trackToPlay)
    }

    override fun getCurrentPosition(): Int = player.getCurrentPosition()

    override fun getCurrentState(): MusicPlayInteractor.PlayState = player.getCurrentState()

}