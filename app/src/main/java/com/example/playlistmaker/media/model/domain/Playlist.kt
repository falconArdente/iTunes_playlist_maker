package com.example.playlistmaker.media.model.domain

import android.net.Uri
import com.example.playlistmaker.search.model.domain.Track

data class Playlist(
    val id: Int,
    val title: String,
    val description: String,
    val imageUri: Uri?,
    val tracks: List<Track>
)
