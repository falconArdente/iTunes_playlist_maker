package com.example.playlistmaker.media.viewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.model.domain.PlaylistsInteractor
import com.example.playlistmaker.search.model.domain.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class PlaylistItemViewModel(
    private val dataSource: PlaylistsInteractor
) : ViewModel() {
    private val mutablePlaylistScreen = MutableLiveData<PlaylistItemScreenState>()
    val playlistScreenToObserve: LiveData<PlaylistItemScreenState> = mutablePlaylistScreen


    fun setPlaylistById(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dataSource.getPlaylistWithTracksById(playlistId)
                .collect { playlist ->
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

    private fun getMinutesOfTrackList(list: List<Track>): Int {
        var millis: Long = 0L
        val minutesFormat = SimpleDateFormat("mm")
        list.forEach { track ->
            millis +=track.duration.toLong()
        }

        return minutesFormat.format(millis).toInt()
    }
}