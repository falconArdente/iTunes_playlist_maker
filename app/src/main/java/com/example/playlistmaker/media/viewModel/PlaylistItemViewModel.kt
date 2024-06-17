package com.example.playlistmaker.media.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
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
import com.example.playlistmaker.media.model.domain.SharePlaylistUseCase
import com.example.playlistmaker.media.view.EditPlaylistFragment
import com.example.playlistmaker.media.view.ui.CanShowPlaylistMessage
import com.example.playlistmaker.media.view.ui.FragmentCanShowDialog
import com.example.playlistmaker.media.view.ui.PlaylistMessage
import com.example.playlistmaker.search.model.domain.SendTrackToPlayerUseCase
import com.example.playlistmaker.search.model.domain.Track
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class PlaylistItemViewModel(
    private val dataSource: PlaylistsInteractor,
    private val trackToPlayerUseCase: SendTrackToPlayerUseCase,
    private val sharePlaylistUseCase: SharePlaylistUseCase,
    appContext: Context,
) : ViewModel(),ViewModelForFragmentShowsDialog {
    private val noTracksString = appContext.getString(R.string.playlist_no_tracks_to_share)
    private val mutablePlaylistScreen = MutableLiveData<PlaylistItemScreenState>()
    val playlistScreenToObserve: LiveData<PlaylistItemScreenState> = mutablePlaylistScreen
    private var fragment: FragmentCanShowDialog? = null
    private var currentPlaylist: Playlist? = null
    private var currentTrack: Track? = null
    private var dataUpdateJob: Job? = null
    private val trackDeletionConfirmationDialog: MaterialAlertDialogBuilder by lazy {
        configureExitConfirmationDialog(
            R.string.delete_track_dialog_title,
            R.string.delete_track_exit_dialog_confirm,
            R.string.delete_track_exit_dialog_reject,
            R.style.DeleteTrackConfirmationDialogTheme,
            (fragment as Fragment).requireContext()
        )
    }

    override fun attachFragmentAtCreation(fragment: FragmentCanShowDialog) {
        this.fragment = fragment
    }

    fun requirePlaylist(): Playlist? = currentPlaylist
    fun deleteTrackSequence(track: Track): Boolean {
        currentTrack = track
        fragment?.showDialog(trackDeletionConfirmationDialog)
        return true
    }

    fun deleteTrack() {
        if (currentPlaylist == null || currentTrack == null) return
        viewModelScope.launch(Dispatchers.IO) {
            dataSource.removeTrackFromPlaylist(currentTrack!!, currentPlaylist!!)
        }
    }

    fun deletePlaylist() {
        dataUpdateJob?.cancel()
        (fragment as Fragment).findNavController().navigateUp()
        if (currentPlaylist != null) viewModelScope.launch(Dispatchers.IO) {
            dataSource.deletePlaylist(currentPlaylist!!)
        }
    }

    fun sharePlaylist() {
        if (currentPlaylist == null) return
        if (currentPlaylist!!.tracks.isNotEmpty()) {
            sharePlaylistUseCase.execute(currentPlaylist!!)
        } else {
            if (fragment != null && fragment is CanShowPlaylistMessage) {
                (fragment as CanShowPlaylistMessage).showMessage(
                    PlaylistMessage.HaveData(noTracksString)
                )
            }
        }
        mutablePlaylistScreen.postValue(
            (mutablePlaylistScreen.value as PlaylistItemScreenState.HaveData).copy(
                isOptionsBottomSheet = false
            )
        )
    }

    fun goToPlay(track: Track) {
        trackToPlayerUseCase.sendToPlayer(track)
    }

    fun goBack(fragment: Fragment) {
        fragment.findNavController().navigateUp()
    }

    fun showOptions(isVisible: Boolean) {
        if (mutablePlaylistScreen.value is PlaylistItemScreenState.Empty) return
        mutablePlaylistScreen.value =
            (mutablePlaylistScreen.value as PlaylistItemScreenState.HaveData).copy(
                isOptionsBottomSheet = isVisible
            )
    }

    fun editPlaylist() {
        if (currentPlaylist == null) return
        (fragment as Fragment).findNavController().navigate(
            R.id.action_playlistView_to_editPlaylistFragment,
            args = EditPlaylistFragment.createArgs(currentPlaylist!!.id)
        )
        (mutablePlaylistScreen.value as PlaylistItemScreenState.HaveData).copy(
            isOptionsBottomSheet = false
        )
    }

    fun setPlaylistById(playlistId: Int) {
        dataUpdateJob?.cancel()
        dataUpdateJob = viewModelScope.launch(Dispatchers.IO) {
            dataSource.getPlaylistWithTracksById(playlistId)
                .collect { playlist ->
                    currentPlaylist = playlist
                    mutablePlaylistScreen.postValue(
                        PlaylistItemScreenState.HaveData(
                            imageUri = playlist.imageUri ?: Uri.EMPTY,
                            title = playlist.title,
                            description = playlist.description,
                            minutes = getMinutesOfTrackList(playlist.tracks),
                            tracksCount = playlist.tracks.size,
                            trackList = playlist.tracks.asReversed()
                        )
                    )
                }
        }

    }

    private fun configureExitConfirmationDialog(
        titleId: Int,
        positiveTextId: Int,
        negativeTextId: Int,
        styleId: Int,
        viewContext: Context,
    ): MaterialAlertDialogBuilder =
        MaterialAlertDialogBuilder(viewContext,  styleId)
            .setTitle(titleId)
            .setPositiveButton(positiveTextId) { _, _ ->
                deleteTrack()
            }
            .setNegativeButton(negativeTextId) { dialogInterface: DialogInterface, _ ->
                dialogInterface.dismiss()
            }

    @SuppressLint("SimpleDateFormat")
    private fun getMinutesOfTrackList(list: List<Track>): Int {
        var millis = 0L
        val minutesFormat = SimpleDateFormat("mm")
        list.forEach { track ->
            millis += track.duration.toLong()
        }
        return minutesFormat.format(millis).toInt()
    }
}