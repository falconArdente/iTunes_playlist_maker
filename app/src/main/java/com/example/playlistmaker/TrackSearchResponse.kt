package com.example.playlistmaker

data class TrackSearchResponse(
    val resultCount: String,
    val results: List<Track>,
)