package com.example.playlistmaker.search.view.ui

import com.example.playlistmaker.search.model.domain.Track
fun interface TrackOnClickListener {
    fun onClick(item: Track)
}