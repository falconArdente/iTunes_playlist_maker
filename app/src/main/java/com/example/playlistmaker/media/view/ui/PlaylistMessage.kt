package com.example.playlistmaker.media.view.ui

sealed class PlaylistMessage {
    data object Empty : PlaylistMessage()
    data class HaveData(
         val message: String,
         val showTimeMillis: Long = 500L
    ): PlaylistMessage()
}
