package com.example.playlistmaker.media.view.ui

import com.example.playlistmaker.media.model.domain.Playlist

fun interface PlaylistOnClickListener {
    fun onClick(item: Playlist)
}