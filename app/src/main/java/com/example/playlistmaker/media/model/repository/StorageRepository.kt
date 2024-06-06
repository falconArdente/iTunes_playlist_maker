package com.example.playlistmaker.media.model.repository

import android.net.Uri

interface StorageRepository {
    fun saveImageByUri(imageUri: Uri,fileName:String): Uri
}