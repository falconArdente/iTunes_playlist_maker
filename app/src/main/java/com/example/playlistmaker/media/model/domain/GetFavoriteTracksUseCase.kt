package com.example.playlistmaker.media.model.domain

import com.example.playlistmaker.search.model.domain.Track

interface GetFavoriteTracksUseCase {
    fun provideTracks():List<Track>
}