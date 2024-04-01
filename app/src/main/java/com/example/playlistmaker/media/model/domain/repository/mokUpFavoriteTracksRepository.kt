package com.example.playlistmaker.media.model.domain.repository

import com.example.playlistmaker.search.model.domain.Track

class mokUpFavoriteTracksRepository : FavoriteTracksRepository {
    override fun getTracks(): List<Track> {
        val tracks = mutableListOf<Track>()
        tracks.add(Track("35", "", "", "", "", "", "", "", "", ""))
        return if (true) tracks.toList() else listOf<Track>()
    }
}