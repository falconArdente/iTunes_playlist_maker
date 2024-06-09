package com.example.playlistmaker.player.view

import com.example.playlistmaker.media.model.domain.Playlist

interface PlaylistViewOnClickListener {
    fun onClick(item: Playlist)
}