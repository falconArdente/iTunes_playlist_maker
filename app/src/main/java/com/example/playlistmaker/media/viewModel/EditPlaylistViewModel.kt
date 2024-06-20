package com.example.playlistmaker.media.viewModel

import android.content.Context
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.media.model.domain.Playlist
import com.example.playlistmaker.media.model.domain.PlaylistsInteractor
import com.example.playlistmaker.media.model.domain.SaveImageToStorageUseCase
import com.example.playlistmaker.media.model.domain.SelectAnImageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    imageSelector: SelectAnImageUseCase,
    saverForImage: SaveImageToStorageUseCase,
    dataTable: PlaylistsInteractor,
    androidContext: Context,
) : CreatePlaylistViewModel(imageSelector, saverForImage, dataTable, androidContext) {
    private var playlistToEdit: Playlist? = null
    fun setPlaylistToEdit(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistToEdit = dataTable.getPlaylistById(playlistId)
            playlistToEdit?.let { playlist ->
                mutableScreenState.postValue(
                    CreatePlaylistScreenState(
                        imageUri = playlist.imageUri ?: Uri.EMPTY,
                        title = playlist.title,
                        description = playlist.description,
                        isReadyToSave = true
                    )
                )
            }
        }
    }

    override fun createPlaylist() {
        if (playlistToEdit==null) return
        with(screenStateToObserve.value as CreatePlaylistScreenState) {
            val uriToDB =
                saverForImage.saveImageByUri(imageUri = imageUri, fileName = title)
            viewModelScope.launch(Dispatchers.IO) {
                dataTable.updatePlaylist(
                    Playlist(
                        id = playlistToEdit!!.id,
                        title = title, description = description,
                        imageUri = uriToDB
                    )
                )
            }
            runExitSequence()
        }
    }

    override fun runExitSequence() {
        (fragment as Fragment).findNavController().navigateUp()
    }
}