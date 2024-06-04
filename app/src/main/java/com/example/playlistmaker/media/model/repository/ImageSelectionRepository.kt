package com.example.playlistmaker.media.model.repository

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface ImageSelectionRepository {
    suspend fun pickAnImage():Flow<Uri>
}