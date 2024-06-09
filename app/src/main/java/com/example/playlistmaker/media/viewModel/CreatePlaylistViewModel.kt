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
import com.example.playlistmaker.media.model.data.db.dao.PlaylistsDao
import com.example.playlistmaker.media.model.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media.model.domain.SaveImageToStorageUseCase
import com.example.playlistmaker.media.model.domain.SelectAnImageUseCase
import com.example.playlistmaker.media.model.repository.SelectAnImageUseCasePickerCompatibleImpl
import com.example.playlistmaker.media.view.MESSAGE_DURATION
import com.example.playlistmaker.media.view.MESSAGE_TEXT
import com.example.playlistmaker.media.view.ui.FragmentWithExitConfirmationDialog
import com.example.playlistmaker.media.view.ui.PlaylistMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val MESSAGE_DELAY = 3000L

class CreatePlaylistViewModel(
    private val imageSelector: SelectAnImageUseCase,
    private val saverForImage: SaveImageToStorageUseCase,
    private val dataTable: PlaylistsDao,
    androidContext: Context,
) : ViewModel() {
    private val playlistCreatedPrefix: String =
        androidContext.getString(R.string.playlist_created_prefix)
    private val playlistCreatedPostfix: String =
        androidContext.getString(R.string.playlist_created_postfix)

    private var mutableScreeState = MutableLiveData(
        CreatePlaylistScreenState(
            title = "", description = "", isReadyToCreate = false
        )
    )
    private var playlistMessage = MutableLiveData<PlaylistMessage>(PlaylistMessage.Empty)
    var finishActivityWhenDone: Boolean = false
    private var fragment: FragmentWithExitConfirmationDialog? = null
    var screenStateToObserve: LiveData<CreatePlaylistScreenState> = mutableScreeState
    var playlistMessageToObserve: LiveData<PlaylistMessage> = playlistMessage
    fun attachFragmentAtCreation(fragment: FragmentWithExitConfirmationDialog) {
        this.fragment = fragment
        if (imageSelector !is SelectAnImageUseCasePickerCompatibleImpl) return
        imageSelector.attachPickerToFragment(fragment as Fragment)
    }

    fun changeTitle(title: String) {
        (mutableScreeState.value as CreatePlaylistScreenState).apply {
            this.title = title
            this.isReadyToCreate = title.isNotEmpty()
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
            imageSelector.selectImage().collect { uri ->
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
            viewModelScope.launch(Dispatchers.IO) {
                dataTable.createPlaylist(
                    PlaylistEntity(
                        title = title, description = description, imageUri = uriToDB.toString()
                    )
                )
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

    fun runExitSequence() {
        if (isNeedDialog()) {
            fragment?.runExitConfirmationDialog()
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

    private fun isNeedDialog(): Boolean {
        (screenStateToObserve.value as CreatePlaylistScreenState).let { state ->
            return (state.title.isNotEmpty() || state.description.isNotEmpty() || state.imageUri != Uri.EMPTY)
        }
    }
}