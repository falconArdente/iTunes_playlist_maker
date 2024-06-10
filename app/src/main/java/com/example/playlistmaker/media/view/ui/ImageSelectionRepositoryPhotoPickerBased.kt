package com.example.playlistmaker.media.view.ui

import android.net.Uri
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.example.playlistmaker.R
import com.example.playlistmaker.media.model.repository.ImageSelectionRepository
import kotlinx.coroutines.flow.Flow

class ImageSelectionRepositoryPhotoPickerBased() :
    ImageSelectionRepository {

    private var fragment: Fragment? = null
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private var mutableUri: MutableLiveData<Uri> = MutableLiveData(Uri.EMPTY)
    fun attachPickerToFragmentLifecycle(fragment: Fragment) {// should run inside fragment`s create section
        if (fragment == null) return
        this.fragment = fragment
        pickMedia =
            fragment.registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    mutableUri.postValue(uri!!)
                } else {
                    Toast.makeText(
                        fragment!!.requireActivity(),
                        fragment!!.requireActivity()
                            .getText(R.string.no_image_been_chosen_message),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    override suspend fun pickAnImage(): Flow<Uri> {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        return mutableUri.asFlow()
    }

}