package com.example.playlistmaker.search.model.domain

data class Track(
    val id: Long,
    val trackTitle: String,
    val artistName: String,
    val duration: String,
    val artwork: String,
    val collectionName: String,
    val releaseDate: String,
    val genre: String,
    val country: String,
    val trackPreview: String,
)