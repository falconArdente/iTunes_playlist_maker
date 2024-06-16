package com.example.playlistmaker.search.view.ui

import com.example.playlistmaker.search.model.domain.Track

fun interface TrackOnLongClickListener{
    fun onLongClick(item: Track):Boolean
}