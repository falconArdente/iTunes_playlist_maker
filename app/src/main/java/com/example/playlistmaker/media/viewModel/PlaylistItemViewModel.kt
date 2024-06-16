package com.example.playlistmaker.media.viewModel

import android.annotation.SuppressLint
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
import com.example.playlistmaker.media.view.EditAndCreatePlaylistFragment
import com.example.playlistmaker.media.view.ui.FragmentWithConfirmationDialog
import com.example.playlistmaker.search.model.domain.SendTrackToPlayerUseCase
import com.example.playlistmaker.search.model.domain.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class PlaylistItemViewModel(
    private val dataSource: PlaylistsInteractor,
    private val trackToPlayerUseCase: SendTrackToPlayerUseCase,
    private val sharePlaylistUseCase: SharePlaylistUseCase,
) : ViewModel() {
    private val mutablePlaylistScreen = MutableLiveData<PlaylistItemScreenState>()
    val playlistScreenToObserve: LiveData<PlaylistItemScreenState> = mutablePlaylistScreen
    private var fragment: FragmentWithConfirmationDialog? = null
    private var currentPlaylist: Playlist? = null
    private var currentTrack: Track? = null
    private var dataUpdateJob: Job? = null
    fun attachFragmentBeforeShowDialog(fragment: FragmentWithConfirmationDialog) {
        this.fragment = fragment
    }

    fun requirePlaylist(): Playlist? = currentPlaylist
    fun deleteTrackSequence(track: Track): Boolean {
        currentTrack = track
        fragment?.runConfirmationDialog()
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
        if (currentPlaylist != null) {
            if (currentPlaylist!!.tracks.isNotEmpty()) sharePlaylistUseCase.execute(currentPlaylist!!)
        }
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
            args = EditAndCreatePlaylistFragment.createArgs(currentPlaylist!!.id)
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
                            trackList = playlist.tracks
                        )
                    )
                }
        }

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