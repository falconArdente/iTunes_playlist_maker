package com.example.playlistmaker.search.model.data.repository

import com.example.playlistmaker.search.model.domain.Track
import com.example.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
}