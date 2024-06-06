package com.example.playlistmaker.media.viewModel

import android.net.Uri

data class CreatePlaylistScreenState(
    var imageUri: Uri = Uri.EMPTY,
    var title: String = "",
    var description: String = "",
    var isReadyToCreate: Boolean = false
)
