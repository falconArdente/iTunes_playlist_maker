package com.example.playlistmaker.media.model.domain

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface SelectAnImageUseCase {
    suspend fun selectImage(): Flow<Uri>
}