package com.example.playlistmaker.player.model.data

import android.media.MediaPlayer
import android.util.Log
import com.example.playlistmaker.player.model.domain.MusicPlayInteractor
import com.example.playlistmaker.player.model.domain.PlayState
import com.example.playlistmaker.player.model.domain.Player
import com.example.playlistmaker.search.model.domain.Track

class MediaPlayerBasedPlayer( private val mediaPlayer:MediaPlayer) : Player {
    private var track: Track? = null
    private var playerState = PlayState.NotReady
        private fun prepare() {
        mediaPlayer.setOnPreparedListener { didPrepared() }
        mediaPlayer.setOnCompletionListener { didPrepared() }
        mediaPlayer.prepareAsync()
    }

    override fun play() {
        if (playerState == PlayState.ReadyToPlay ||
            playerState == PlayState.Paused ||
            playerState == PlayState.Playing
        ) {
            mediaPlayer.start()
            playerState = PlayState.Playing
            playEventsConsumer?.playEventConsume()
        } else Log.e("player", "try to play with $playerState")
    }

    override fun pause() {
        if (playerState == PlayState.ReadyToPlay ||
            playerState == PlayState.Paused ||
            playerState == PlayState.Playing
        ) {
            mediaPlayer.pause()
            playerState = PlayState.Paused
            playEventsConsumer?.pauseEventConsume()
        } else Log.e("player", "try to pause with $playerState")
    }

    override fun stop() {
        if (playerState != PlayState.NotReady) {
            mediaPlayer.stop()
            didPrepared()
        } else Log.e("player", "try to play with $playerState")
    }

    override fun setTrack(trackToPlay: Track) {
        track = trackToPlay
        if (playerState != PlayState.NotReady) mediaPlayer.release()
        mediaPlayer.setDataSource(track?.trackPreview)
        prepare()
    }

    override fun getCurrentState(): PlayState = playerState

    override fun getCurrentPosition(): Int = mediaPlayer.currentPosition

    override fun destroy() = mediaPlayer.release()

    private fun didPrepared() {
        playerState = PlayState.ReadyToPlay
        playEventsConsumer?.readyToPlayEventConsume()
    }

    override fun setConsumer(playEventsConsumer: MusicPlayInteractor.MusicPlayEventsConsumer) {
        this.playEventsConsumer = playEventsConsumer
    }

    private var playEventsConsumer: MusicPlayInteractor.MusicPlayEventsConsumer? = null
}