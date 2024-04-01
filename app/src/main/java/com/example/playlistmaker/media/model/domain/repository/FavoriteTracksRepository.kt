package com.example.playlistmaker.media.model.domain.repository

import com.example.playlistmaker.search.model.domain.Track

interface FavoriteTracksRepository {
    fun getTracks():List<Track>
}