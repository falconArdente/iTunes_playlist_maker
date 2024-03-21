package com.example.playlistmaker.search.model.domain

interface TracksConsumer {
    fun consume(foundTracks: List<Track>)
}