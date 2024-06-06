package com.example.playlistmaker.media.viewModel

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.media.model.data.db.dao.PlaylistsDao
import com.example.playlistmaker.media.model.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media.model.domain.SaveImageToStorageUseCase
import com.example.playlistmaker.media.model.domain.SelectAnImageUseCase
import com.example.playlistmaker.media.model.repository.SelectAnImageUseCasePickerCompatibleImpl
import com.example.playlistmaker.media.view.ui.FragmentWithExitConfirmationDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    private val androidContext: Context,
    private val imageSelector: SelectAnImageUseCase,
    private val saverForImage: SaveImageToStorageUseCase,
    private val dataTable: PlaylistsDao
) : ViewModel() {
    private var mutableScreeState: MutableLiveData<CreatePlaylistScreenState>

    init {
        with(androidContext) {
            mutableScreeState = MutableLiveData(
                CreatePlaylistScreenState(
                    title = getString(R.string.create_playlist_title_field),
                    description = getString(R.string.create_playlist_description_field),
                    isReadyToCreate = false
                )
            )
        }
    }

    private var fragment: FragmentWithExitConfirmationDialog? = null
    var screenStateToObserve: LiveData<CreatePlaylistScreenState> = mutableScreeState
    fun attachFragmentAtCreation(fragment: FragmentWithExitConfirmationDialog) {
        this.fragment = fragment
        if (imageSelector !is SelectAnImageUseCasePickerCompatibleImpl) return
        imageSelector.attachPickerToFragment(fragment as Fragment)
    }

    fun changeTitle(title: String) {
        (mutableScreeState.value as CreatePlaylistScreenState).apply {
            this.title = title
            this.isReadyToCreate =
                !(title.isEmpty() || title == androidContext.getString(R.string.create_playlist_title_field))
            mutableScreeState.postValue(this)
        }
    }

    fun changeDescription(description: String) {
        (mutableScreeState.value as CreatePlaylistScreenState).let { state ->
            state.description = description
            mutableScreeState.postValue(state)
        }
    }

    fun selectAnImage() {
        viewModelScope.launch(Dispatchers.IO) {
            imageSelector
                .selectImage()
                .collect { uri ->
                    (mutableScreeState.value as CreatePlaylistScreenState).let { state ->
                        state.imageUri = uri
                        mutableScreeState.postValue(state)
                    }
                }
        }
    }

    fun createPlaylist() {
        with(screenStateToObserve.value as CreatePlaylistScreenState) {
            val uriToDB = saverForImage.saveImageByUri(imageUri = imageUri, fileName = title)
            val descriptionForDB =
                if (description == androidContext.getString(R.string.create_playlist_description_field))
                    "" else description
            viewModelScope.launch(Dispatchers.IO) {
                dataTable.createPlaylist(
                    PlaylistEntity(
                        title = title,
                        description = descriptionForDB,
                        imageUri = uriToDB.toString()
                    )
                )
            }
            Toast.makeText(
                androidContext,
                androidContext.getString(R.string.playlist_created_prefix) +
                        title + androidContext.getString(R.string.playlist_created_postfix),
                Toast.LENGTH_LONG
            ).show()
            (fragment as Fragment).findNavController().navigateUp()
        }
    }

    fun exitSequence() {
        (screenStateToObserve.value as CreatePlaylistScreenState).let { state ->
            if (state.title.isNotEmpty() ||
                state.description.isNotEmpty() ||
                state.imageUri != Uri.EMPTY
            ) {
                fragment?.runExitConfirmationDialog()
            } else (fragment as Fragment).findNavController().navigateUp()
        }
    }
}
