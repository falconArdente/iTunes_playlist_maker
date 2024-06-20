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
) : ViewModel(), ViewModelForFragmentShowsDialog {
    private val noTracksString = appContext.getString(R.string.playlist_no_tracks_to_share)
    private val mutablePlaylistScreen = MutableLiveData<PlaylistItemScreenState>()
    val playlistScreenToObserve: LiveData<PlaylistItemScreenState> = mutablePlaylistScreen
    private var fragment: FragmentCanShowDialog? = null
    private var currentPlaylist: Playlist? = null
    private var currentTrack: Track? = null
    private var dataUpdateJob: Job? = null
    private val trackDeletionConfirmationDialog: MaterialAlertDialogBuilder by lazy {
        configureConfirmationDialog(
            title = appContext.getString(R.string.delete_track_dialog_title),
            positiveText = appContext.getString(R.string.delete_track_exit_dialog_confirm),
            actionPositive = { deleteTrack() },
            negativeText = appContext.getString(R.string.delete_track_exit_dialog_reject),
            styleId = R.style.DeleteTrackConfirmationDialogTheme,
            viewContext = (fragment as Fragment).requireContext()
        )
    }
    private val playlistDeletionConfirmationDialog: MaterialAlertDialogBuilder by lazy {
        configureConfirmationDialog(
            title = appContext.getString(R.string.delete_playlist_dialog_title),
            message = appContext.getString(R.string.delete_playlist_dialog_message_prefix) +
                    (playlistScreenToObserve.value as PlaylistItemScreenState.HaveData).title +
                    appContext.getString(R.string.delete_playlist_dialog_message_postfix),
            positiveText = appContext.getString(R.string.delete_playlist_dialog_title_positive),
            actionPositive = { deletePlaylist() },
            negativeText = appContext.getString(R.string.delete_playlist_dialog_title_negative),
            styleId = R.style.DeleteTrackConfirmationDialogTheme,
            viewContext = (fragment as Fragment).requireContext()
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

    private fun deleteTrack() {
        if (currentPlaylist == null || currentTrack == null) return
        viewModelScope.launch(Dispatchers.IO) {
            dataSource.removeTrackFromPlaylist(currentTrack!!, currentPlaylist!!)
        }
    }

    fun deletePlaylistSequence() {
        fragment?.showDialog(playlistDeletionConfirmationDialog)
    }

    private fun deletePlaylist() {
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
        mutablePlaylistScreen.postValue(
            (mutablePlaylistScreen.value as PlaylistItemScreenState.HaveData).copy(
                isOptionsBottomSheet = false
            )
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

    private fun configureConfirmationDialog(
        title: String,
        message: String = "",
        positiveText: String,
        negativeText: String,
        styleId: Int,
        actionPositive: DialogCallback,
        viewContext: Context,
    ): MaterialAlertDialogBuilder {
        val dialog = MaterialAlertDialogBuilder(viewContext, styleId)
            .setTitle(title)
            .setPositiveButton(positiveText) { _, _ ->
                actionPositive.execute()
            }
            .setNegativeButton(negativeText) { dialogInterface: DialogInterface, _ ->
                dialogInterface.dismiss()
            }
        if (message != "") dialog.setMessage(message)
        return dialog
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

    fun interface DialogCallback {
        fun execute()
    }
}