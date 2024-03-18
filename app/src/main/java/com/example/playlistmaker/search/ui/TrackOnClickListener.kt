package com.example.playlistmaker.search.ui

import com.example.playlistmaker.search.domain.Track
interface TrackOnClickListener {
    fun onClick(item: Track)
}