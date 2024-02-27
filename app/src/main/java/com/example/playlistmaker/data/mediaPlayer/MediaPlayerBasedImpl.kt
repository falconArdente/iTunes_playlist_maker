package com.example.playlistmaker.data.mediaPlayer

import android.media.MediaPlayer
import android.util.Log
import com.example.playlistmaker.domain.api.MusicPlayInteractor
import com.example.playlistmaker.domain.models.Track

class MediaPlayerBasedImpl : Player {
    private lateinit var track: Track
    private var playerState = MusicPlayInteractor.PlayState.NotReady
    private val mediaPlayer = MediaPlayer()
    private fun prepare() {
        mediaPlayer.setOnPreparedListener { didPrepared() }
        mediaPlayer.setOnCompletionListener { didPrepared() }
        mediaPlayer.prepareAsync()
    }

    override fun play() {
        if (playerState == MusicPlayInteractor.PlayState.ReadyToPlay ||
            playerState == MusicPlayInteractor.PlayState.Paused ||
            playerState == MusicPlayInteractor.PlayState.Playing
        ) {
            mediaPlayer.start()
            playerState = MusicPlayInteractor.PlayState.Playing
            playEventsConsumer?.playEventConsume()
        } else Log.e("player", "try to play with $playerState")
    }

    override fun pause() {
        if (playerState == MusicPlayInteractor.PlayState.ReadyToPlay ||
            playerState == MusicPlayInteractor.PlayState.Paused ||
            playerState == MusicPlayInteractor.PlayState.Playing
        ) {
            mediaPlayer.pause()
            playerState = MusicPlayInteractor.PlayState.Paused
            playEventsConsumer?.pauseEventConsume()
        } else Log.e("player", "try to pause with $playerState")
    }

    override fun stop() {
        if (playerState != MusicPlayInteractor.PlayState.NotReady) {
            mediaPlayer.stop()
            didPrepared()
        } else Log.e("player", "try to play with $playerState")
    }

    override fun setTrack(trackToPlay: Track) {
        track = trackToPlay
        if (playerState != MusicPlayInteractor.PlayState.NotReady) mediaPlayer.release()
        mediaPlayer.setDataSource(track.trackPreview)
        prepare()
    }

    override fun getCurrentState(): MusicPlayInteractor.PlayState {
        return playerState
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun destroy() {
        mediaPlayer.release()
    }

    private fun didPrepared() {
        playerState = MusicPlayInteractor.PlayState.ReadyToPlay
        playEventsConsumer?.readyToPlayEventConsume()
    }

    override fun setConsumer(playEventsConsumer: MusicPlayInteractor.MusicPlayEventsConsumer) {
        this.playEventsConsumer = playEventsConsumer
    }

    private var playEventsConsumer: MusicPlayInteractor.MusicPlayEventsConsumer? = null
}