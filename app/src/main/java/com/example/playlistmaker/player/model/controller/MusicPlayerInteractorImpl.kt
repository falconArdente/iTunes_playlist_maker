package com.example.playlistmaker.player.model.controller

import com.example.playlistmaker.player.model.domain.MusicPlayInteractor
import com.example.playlistmaker.player.model.domain.PlayState
import com.example.playlistmaker.player.model.domain.Player
import com.example.playlistmaker.search.model.domain.Track

class MusicPlayerInteractorImpl(
    private val player: Player,
    musicPlayEventsConsumer: MusicPlayInteractor.MusicPlayEventsConsumer? = null
) : MusicPlayInteractor {
    init {
        if (musicPlayEventsConsumer != null) player.setConsumer(musicPlayEventsConsumer)
    }

    override fun play() = player.play()
    override fun pause() = player.pause()
    override fun stop() = player.stop()
    override fun setTrack(trackToPlay: Track) = player.setTrack(trackToPlay)
    override fun setConsumer(consumer: MusicPlayInteractor.MusicPlayEventsConsumer) =
        player.setConsumer(consumer)

    override fun destroy() = player.destroy()
    override fun getCurrentPosition(): Int = player.getCurrentPosition()

    override fun getCurrentState(): PlayState = player.getCurrentState()

}