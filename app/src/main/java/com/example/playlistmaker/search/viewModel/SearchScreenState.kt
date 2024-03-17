package com.example.playlistmaker.search.viewModel

import com.example.playlistmaker.search.domain.Track

sealed class SearchScreenState {
    data class HistoryHaveData(val tracks: List<Track>) : SearchScreenState()
    object HistoryIsEmpty : SearchScreenState()
    data class ResultHaveData(val tracks: List<Track>) : SearchScreenState()
    object ResultIsEmpty : SearchScreenState()
    object Error : SearchScreenState()
    object Loading : SearchScreenState()
}
