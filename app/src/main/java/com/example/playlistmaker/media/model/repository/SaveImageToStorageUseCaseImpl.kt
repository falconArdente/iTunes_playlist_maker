package com.example.playlistmaker.media.model.repository

import android.net.Uri
import com.example.playlistmaker.media.model.domain.SaveImageToStorageUseCase

class SaveImageToStorageUseCaseImpl(private val repository: StorageRepository) :
    SaveImageToStorageUseCase {
    override fun saveImageByUri(imageUri: Uri, fileName: String):Uri =
        repository.saveImageByUri(imageUri, fileName)
}