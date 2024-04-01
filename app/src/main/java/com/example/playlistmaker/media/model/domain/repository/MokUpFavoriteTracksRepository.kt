package com.example.playlistmaker.media.model.domain.repository

import com.example.playlistmaker.search.model.domain.Track

class MokUpFavoriteTracksRepository : FavoriteTracksRepository {
    override fun getTracks(): List<Track> {
        val tracks = mutableListOf<Track>()
        tracks.add(Track("35", "", "", "", "", "", "", "", "", ""))
        return if (false) tracks.toList() else listOf()//waits relisation income
    }
}