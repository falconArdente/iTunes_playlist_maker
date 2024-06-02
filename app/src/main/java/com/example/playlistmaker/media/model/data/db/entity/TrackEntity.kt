package com.example.playlistmaker.media.model.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

private const val TABLE_NAME = "favorite_tracks_table"

@Entity(tableName = TABLE_NAME, indices = [Index(value = arrayOf("remoteId"), unique = true)])
data class TrackEntity(
    @PrimaryKey(autoGenerate = true) val localId: Int? = null,
    @ColumnInfo(name = "remoteId", typeAffinity = ColumnInfo.INTEGER) val remoteId: Long,
    @ColumnInfo(name = "trackName") val trackName: String,
    @ColumnInfo(name = "artistName") val artistName: String,
    @ColumnInfo(name = "trackTimeMillis") val trackTimeMillis: String,
    @ColumnInfo(name = "artworkUrl100") val artworkUrl100: String,
    @ColumnInfo(name = "collectionName") val collectionName: String,
    @ColumnInfo(name = "releaseDate") val releaseDate: String,
    @ColumnInfo(name = "primaryGenreName") val genre: String,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "previewUrl") val previewUrl: String,
    @ColumnInfo(
        name = "dateOfChange",
        typeAffinity = ColumnInfo.INTEGER,
        defaultValue = "CURRENT_TIMESTAMP"
    )
    var dateOfChange: Long? = null,
)
