package com.example.playlistmaker.media.model.domain

import android.net.Uri

interface SaveImageToStorageUseCase {
    fun saveImageByUri(imageUri: Uri,fileName:String):Uri
}