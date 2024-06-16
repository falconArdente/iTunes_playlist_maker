package com.example.playlistmaker.media.viewModel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.media.model.domain.Playlist
import com.example.playlistmaker.media.model.domain.PlaylistsInteractor
import com.example.playlistmaker.media.model.domain.SaveImageToStorageUseCase
import com.example.playlistmaker.media.model.domain.SelectAnImageUseCase
import com.example.playlistmaker.media.model.repository.SelectAnImageUseCasePickerCompatibleImpl
import com.example.playlistmaker.media.view.MESSAGE_DURATION
import com.example.playlistmaker.media.view.MESSAGE_TEXT
import com.example.playlistmaker.media.view.ui.FragmentWithConfirmationDialog
import com.example.playlistmaker.media.view.ui.PlaylistMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val MESSAGE_DELAY = 3000L

class EditAndCreatePlaylistViewModel(
    private val imageSelector: SelectAnImageUseCase,
    private val saverForImage: SaveImageToStorageUseCase,
    private val dataTable: PlaylistsInteractor,
    androidContext: Context,
) : ViewModel() {
    private val playlistCreatedPrefix: String =
        androidContext.getString(R.string.playlist_created_prefix)
    private val playlistCreatedPostfix: String =
        androidContext.getString(R.string.playlist_created_postfix)

    private var mutableScreeState = MutableLiveData(
        EditAndCreatePlaylistScreenState(
            title = "", description = "", isReadyToSave = false, isEditMode = false
        )
    )
    private var playlistToEdit: Playlist? = null
    private var playlistMessage = MutableLiveData<PlaylistMessage>(PlaylistMessage.Empty)
    var finishActivityWhenDone: Boolean = false
    private var fragment: FragmentWithConfirmationDialog? = null
    var screenStateToObserve: LiveData<EditAndCreatePlaylistScreenState> = mutableScreeState
    var playlistMessageToObserve: LiveData<PlaylistMessage> = playlistMessage

    fun setPlaylistToEdit(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistToEdit = dataTable.getPlaylistById(playlistId)
            playlistToEdit!!.let { playlist ->
                mutableScreeState.postValue(
                    EditAndCreatePlaylistScreenState(
                        imageUri = playlist.imageUri ?: Uri.EMPTY,
                        title = playlist.title,
                        description = playlist.description,
                        isEditMode = true
                    )
                )
            }
        }
    }

    fun attachFragmentAtCreation(fragment: FragmentWithConfirmationDialog) {
        this.fragment = fragment
        if (imageSelector !is SelectAnImageUseCasePickerCompatibleImpl) return
        imageSelector.attachPickerToFragment(fragment as Fragment)
    }

    fun changeTitle(title: String) {
        mutableScreeState.postValue(
            (mutableScreeState.value as EditAndCreatePlaylistScreenState).copy(
                title = title, isReadyToSave = title.isNotEmpty()
            )
        )
    }

    fun changeDescription(description: String) {
        mutableScreeState.postValue(
            (mutableScreeState.value as EditAndCreatePlaylistScreenState).copy(
                description = description
            )
        )
    }

    fun selectAnImage() {
        viewModelScope.launch(Dispatchers.IO) {
            imageSelector.selectImage().collect { uri ->
                mutableScreeState.postValue(
                    (mutableScreeState.value as EditAndCreatePlaylistScreenState).copy(
                        imageUri = uri
                    )
                )
            }
        }
    }

    fun runExitSequence() {
        if (isNeedDialog()) {
            fragment?.runConfirmationDialog()
        } else {
            exitView()
        }
    }

    fun exitView() {
        with((fragment as Fragment)) {
            if (finishActivityWhenDone) requireActivity().finish()
            else findNavController().navigateUp()
        }
    }

    fun createPlaylist() {
        with(screenStateToObserve.value as EditAndCreatePlaylistScreenState) {
            val uriToDB =
                saverForImage.saveImageByUri(imageUri = imageUri, fileName = title)
            viewModelScope.launch(Dispatchers.IO) {
                if (isEditMode) {
                    dataTable.updatePlaylist(
                        Playlist(
                            id = playlistToEdit!!.id,
                            title = title, description = description,
                            imageUri = uriToDB
                        )
                    )
                } else {//Create mode
                    dataTable.createPlaylist(
                        title = title, description = description, imageUri = uriToDB
                    )
                }
            }

            if (finishActivityWhenDone) {
                ((fragment as Fragment).requireActivity()).let { activity ->
                    val intent: Intent = activity.intent.putExtra(
                        MESSAGE_TEXT, createMessageText(title)
                    ).putExtra(MESSAGE_DURATION, MESSAGE_DELAY)
                    activity.setResult(Activity.RESULT_OK, intent)
                }
            } else {
                playlistMessage.postValue(
                    PlaylistMessage.HaveData(
                        message = createMessageText(title), showTimeMillis = MESSAGE_DELAY
                    )
                )
            }
            exitView()
        }
    }

    private fun createMessageText(playlistName: String): String =
        "$playlistCreatedPrefix $playlistName $playlistCreatedPostfix"

    private fun isNeedDialog(): Boolean {
        (screenStateToObserve.value as EditAndCreatePlaylistScreenState).let { state ->
            return (state.title.isNotEmpty() || state.description.isNotEmpty() || state.imageUri != Uri.EMPTY)
        }
    }
}