package com.example.playlistmaker.media.viewModel

import android.net.Uri

data class EditAndCreatePlaylistScreenState(
    val imageUri: Uri = Uri.EMPTY,
    val title: String = "",
    val description: String = "",
    val isReadyToSave: Boolean = false,
    val isEditMode: Boolean=false,
)
