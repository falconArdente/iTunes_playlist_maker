package com.example.playlistmaker.media.model.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

private const val TRACKS_TABLE = "playlists_tracks_table"

@Entity(
    tableName = TRACKS_TABLE
)
data class PlaylistTrackEntity(
    @PrimaryKey(autoGenerate = true) val pTrackId: Int? = null,
    @ColumnInfo(
        name = "playlistRelationId", typeAffinity = ColumnInfo.INTEGER
    ) val playlistRelationId: Int,
    @ColumnInfo(name = "remoteId", typeAffinity = ColumnInfo.INTEGER) val remoteId: Long,
    @ColumnInfo(name = "trackName", defaultValue = "") val trackName: String,
    @ColumnInfo(name = "artistName", defaultValue = "") val artistName: String,
    @ColumnInfo(name = "trackTimeMillis", defaultValue = "") val trackTimeMillis: String,
    @ColumnInfo(name = "artworkUrl100", defaultValue = "") val artworkUrl100: String,
    @ColumnInfo(name = "collectionName", defaultValue = "") val collectionName: String,
    @ColumnInfo(name = "releaseDate", defaultValue = "") val releaseDate: String,
    @ColumnInfo(name = "primaryGenreName", defaultValue = "") val genre: String,
    @ColumnInfo(name = "country", defaultValue = "") val country: String,
    @ColumnInfo(name = "previewUrl", defaultValue = "") val previewUrl: String,
)