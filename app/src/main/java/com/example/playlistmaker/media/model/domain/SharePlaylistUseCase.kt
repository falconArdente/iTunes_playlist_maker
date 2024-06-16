package com.example.playlistmaker.media.model.domain

fun interface SharePlaylistUseCase {
    fun execute(playlistToShare: Playlist)
}