package com.example.playlistmaker.player.viewModel

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.media.model.domain.Playlist
import com.example.playlistmaker.media.model.domain.PlaylistsInteractor
import com.example.playlistmaker.media.view.ui.PlaylistMessage
import com.example.playlistmaker.media.viewModel.MESSAGE_DELAY
import com.example.playlistmaker.player.model.domain.CurrentFavoriteTrackInteractor
import com.example.playlistmaker.player.model.domain.GetTrackToPlayUseCase
import com.example.playlistmaker.player.model.domain.MusicPlayInteractor
import com.example.playlistmaker.player.model.domain.PlayState
import com.example.playlistmaker.player.view.ui.PlaylistViewOnClickListener
import com.example.playlistmaker.search.model.domain.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val player: MusicPlayInteractor,
    private val currentFavoriteInteractor: CurrentFavoriteTrackInteractor,
    private val playlistDataSource: PlaylistsInteractor,
    appContext: Context
) : ViewModel() {
    companion object {
        private const val DURATION_RENEWAL_DELAY: Long = 300L
        private const val ADD_TRACK_BOTTOMSHEET_HIDE_DELAY: Long = 600L
    }

    private val addToPlaylistSuccessPrefix: String =
        appContext.getString(R.string.add_to_playlist_success_prefix)
    private val alreadyHaveInPlaylistPrefix: String =
        appContext.getString(R.string.alredy_have_in_playlist_prefix)

    init {
        player.setConsumer(object : MusicPlayInteractor.MusicPlayEventsConsumer {
            override fun playEventConsume() {
                playerScreenState.value =
                    getPlayerScreenState().value?.copy(playState = PlayState.Playing)
                durationUpdateJob = viewModelScope.launch {
                    while (true) {
                        playerScreenState.value =
                            getPlayerScreenState().value?.copy(currentPosition = player.getCurrentPosition())
                        delay(DURATION_RENEWAL_DELAY)
                    }
                }
            }

            override fun pauseEventConsume() {
                durationUpdateJob?.cancel()
                playerScreenState.value =
                    getPlayerScreenState().value?.copy(playState = PlayState.Paused)
            }

            override fun readyToPlayEventConsume() {
                durationUpdateJob?.cancel()
                playerScreenState.value = getPlayerScreenState().value?.copy(
                    playState = PlayState.ReadyToPlay, currentPosition = 0
                )
            }
        })
    }

    private val playerScreenState = MutableLiveData(PlayerScreenState())
    private var durationUpdateJob: Job? = null
    private val isFavoriteLiveData = MutableLiveData(false)
    private var isFavoriteDataInteractionJob: Job? = null
    private val mutablePlaylists = MutableLiveData<List<Playlist>>(emptyList())
    private val mutablePlaylistMessage = MutableLiveData<PlaylistMessage>(PlaylistMessage.Empty)
    val playlistsToObserve: LiveData<List<Playlist>> = mutablePlaylists
    val messageToObserve: LiveData<PlaylistMessage> = mutablePlaylistMessage
    private var playlistDataIntercourseJob: Job? = null
    private var bottomSheetBehaviorDelegate: BottomSheetBehavior<ConstraintLayout>? = null
    fun getPlaylistViewOnClickListener() = object : PlaylistViewOnClickListener {
        override fun onClick(item: Playlist) {
            viewModelScope.launch(Dispatchers.IO) {
                val resultForAdd = playlistDataSource.addTrackToPlaylist(
                    trackToAdd = (playerScreenState.value as PlayerScreenState).track,
                    playlist = item
                )
                if (resultForAdd) {
                    viewModelScope.launch(Dispatchers.Main) {
                        mutablePlaylistMessage.value = PlaylistMessage.HaveData(
                            message = "$addToPlaylistSuccessPrefix ${item.title}", MESSAGE_DELAY
                        )
                        delay(ADD_TRACK_BOTTOMSHEET_HIDE_DELAY)
                        bottomSheetBehaviorDelegate?.state = BottomSheetBehavior.STATE_HIDDEN
                    }
                } else {
                    mutablePlaylistMessage.postValue(
                        PlaylistMessage.HaveData(
                            message = "$alreadyHaveInPlaylistPrefix ${item.title}", MESSAGE_DELAY
                        )
                    )
                }
            }
        }
    }

    fun addToPlaylistButtonAction(bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>) {
        bottomSheetBehaviorDelegate = bottomSheetBehavior
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        if (playlistDataIntercourseJob == null) playlistDataIntercourseJob =
            viewModelScope.launch(Dispatchers.IO) {
                playlistDataSource.getAllPlaylists().collect {
                        mutablePlaylists.postValue(it)
                    }
            }
    }

    fun getIsFavorite(): LiveData<Boolean> = isFavoriteLiveData

    fun play() = player.play()
    fun pause() = player.pause()

    fun getPlayerScreenState(): LiveData<PlayerScreenState> = playerScreenState
    fun setTrackProvider(provider: GetTrackToPlayUseCase) {
        val track: Track = provider.getTrackToPlay()
        if (track != getPlayerScreenState().value?.track) {
            player.setTrack(track)
            currentFavoriteInteractor.setCurrentTrack(track)
            reLaunchIsFavoriteDataInteraction()
            playerScreenState.postValue(
                PlayerScreenState(
                    track, player.getCurrentState(), player.getCurrentPosition()
                )
            )
        }
    }

    private fun reLaunchIsFavoriteDataInteraction() {
        isFavoriteDataInteractionJob?.cancel()
        isFavoriteDataInteractionJob = viewModelScope.launch(Dispatchers.IO) {
            currentFavoriteInteractor.isTrackFavorite().collect { isFavoriteLiveData.postValue(it) }
        }
    }

    fun addToFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            currentFavoriteInteractor.addTrackToFavorites()
        }
    }

    fun removeFromFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            currentFavoriteInteractor.removeTrackFromFavorites()
        }
    }

    override fun onCleared() {
        player.destroy()
        super.onCleared()
    }
}