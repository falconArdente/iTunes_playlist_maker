package com.example.playlistmaker.media.model.repository

import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.playlistmaker.media.model.domain.SelectAnImageUseCase
import com.example.playlistmaker.media.view.ui.ImageSelectionRepositoryPhotoPickerBased
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SelectAnImageUseCaseImpl(private val repository: ImageSelectionRepository) :
    SelectAnImageUseCase {
    var isNeedToBeAttached: Boolean = false
        get() = repository is ImageSelectionRepositoryPhotoPickerBased

    override suspend fun selectImage(): Flow<Uri> {
        return when (repository) {
            is ImageSelectionRepositoryPhotoPickerBased -> {//this repo should be attached to fragment
                repository.pickAnImage().flowOn(Dispatchers.IO)
            }

            else -> {
                flow {
                    Log.e("Image", "Where is no legal repository provided")
                    emit(Uri.EMPTY)
                }
            }
        }
    }

    fun attachPickerToFragment(fragment: Fragment) {
        when (repository) {
            is ImageSelectionRepositoryPhotoPickerBased -> repository.attachPickerToFragmentLifecycle(
                fragment
            )

            else -> return
        }
    }
}