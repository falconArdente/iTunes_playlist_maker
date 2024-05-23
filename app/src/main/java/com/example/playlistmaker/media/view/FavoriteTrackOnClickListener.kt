package com.example.playlistmaker.media.view

import com.example.playlistmaker.search.model.domain.Track

interface FavoriteTrackOnClickListener {
    fun onClick(item: Track)
}