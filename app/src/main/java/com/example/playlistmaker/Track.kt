package com.example.playlistmaker

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Track(
    val id: String,
    val type: String,
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis")
    val duration: String,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
):Serializable