package com.example.playlistmaker.search.view.ui

import com.example.playlistmaker.search.model.domain.Track
interface TrackOnClickListener {
    fun onClick(item: Track)
}