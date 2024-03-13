package com.example.playlistmaker.player.data

import com.example.playlistmaker.player.domain.MusicPlayInteractor
import com.example.playlistmaker.player.domain.PlayState
import com.example.playlistmaker.player.domain.Player
import com.example.playlistmaker.search.domain.Track

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

    override fun getCurrentState(): PlayState = player.getCurrentState()

}