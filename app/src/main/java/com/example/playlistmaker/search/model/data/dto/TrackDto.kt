package com.example.playlistmaker.search.model.data.dto

import com.google.gson.annotations.SerializedName

data class TrackDto(
    @SerializedName("trackId")
    val remoteId: String,
    @SerializedName("wrapperType")
    val type: String,
    val trackName: String,//trackTitle
    val artistName: String,
    val trackTimeMillis: String,//duration
    val artworkUrl100: String,//artwork
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,//genre
    val country: String,
    val previewUrl: String,//trackPreview
)