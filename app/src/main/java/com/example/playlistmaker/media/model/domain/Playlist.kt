package com.example.playlistmaker.media.model.domain

import android.net.Uri

data class Playlist(
    val id: Int,
    val title: String,
    val description: String,
    val imageUri: Uri?,
)
