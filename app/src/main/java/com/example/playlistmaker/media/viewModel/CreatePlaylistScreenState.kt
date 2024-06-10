package com.example.playlistmaker.media.viewModel

import android.net.Uri

data class CreatePlaylistScreenState(
    val imageUri: Uri = Uri.EMPTY,
    val title: String = "",
    val description: String = "",
    val isReadyToCreate: Boolean = false
)
