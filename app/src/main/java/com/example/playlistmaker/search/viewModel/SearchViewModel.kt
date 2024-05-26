package com.example.playlistmaker.search.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.model.domain.HistoryInteractor
import com.example.playlistmaker.search.model.domain.SearchInteractor
import com.example.playlistmaker.search.model.domain.SendTrackToPlayerUseCase
import com.example.playlistmaker.search.model.domain.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

private const val AUTO_SEND_REQUEST_DELAY = 2000L

class SearchViewModel(
    private val history: HistoryInteractor,
    private val search: SearchInteractor,
    private val trackToPlayer: SendTrackToPlayerUseCase
) : ViewModel() {
    private val screenState = MutableLiveData<SearchScreenState>(SearchScreenState.Loading)
    private var searchPrompt: String = "".apply { showHistory() }
    private var autoSearchJob: Job? = null
    private var searchJob: Job? = null

    fun getScreenState(): LiveData<SearchScreenState> = screenState
    fun doSearchTracks(prompt: String) {
        if (prompt.isEmpty() || searchPrompt.compareTo(prompt) == 0) return
        searchPrompt = prompt
        autoSearchJob?.cancel()
        if (screenState.value != SearchScreenState.Loading) screenState.postValue(
            SearchScreenState.Loading
        )
        searchJob=viewModelScope.launch {
            search
                .searchTracks(expression = searchPrompt).flowOn(Dispatchers.IO)
                .collect { pair ->
                    processSearchResult(pair.first, pair.second)
                }
        }
    }

    private fun processSearchResult(tracks: List<Track>?, errorMessage: String?) {
        if (tracks == null || errorMessage != null)
            screenState.value = SearchScreenState.Error
        else {
            if (tracks.isEmpty()) screenState.value = SearchScreenState.ResultIsEmpty
            else screenState.value = SearchScreenState.ResultHaveData(tracks)
        }
    }

    fun showHistory() {
        val tracks = history.getTracksHistory()
        screenState.postValue(
            if (tracks.isEmpty()) SearchScreenState.HistoryIsEmpty
            else SearchScreenState.HistoryHaveData(tracks)
        )
    }

    fun doAutoSearchTracks(prompt: String) {
        autoSearchJob?.cancel()
        autoSearchJob = viewModelScope.launch {
            delay(AUTO_SEND_REQUEST_DELAY)
            doSearchTracks(prompt)
        }
    }

    fun doRepeatSearch() {
        doSearchTracks(searchPrompt)
    }

    fun cancelSearchSequence() {
        autoSearchJob?.cancel()
        searchJob?.cancel()
    }
    fun addTrackToHistory(track: Track) {
        history.addTrackToHistory(track)
    }

    fun doClearHistory() {
        screenState.postValue(SearchScreenState.HistoryIsEmpty)
        history.clearHistory()
    }

    fun openTrack(trackToOpen: Track) {
        trackToPlayer.sendToPlayer(trackToOpen)
    }
}