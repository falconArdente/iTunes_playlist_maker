package com.example.playlistmaker.search.data.dto

data class TracksSearchResponse(
    var expression: String,
    val resultCount: String,
    val results: List<TrackDto>
) : Response()
