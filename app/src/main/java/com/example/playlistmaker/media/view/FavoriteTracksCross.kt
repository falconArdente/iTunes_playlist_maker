package com.example.playlistmaker.media.view

import com.example.playlistmaker.search.model.domain.Track

interface FavoriteTracksCross {
    fun setTracks(favoriteTracksList:List<Track>)
    fun getfavoriteTracksList():List<Track>
}