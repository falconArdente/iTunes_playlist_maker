package com.example.playlistmaker.search.view

import com.example.playlistmaker.search.model.domain.Track
interface TrackOnClickListener {
    fun onClick(item: Track)
}