package com.example.playlistmaker.media.model.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

private const val TABLE_NAME = "playlists_table"

@Entity(tableName = TABLE_NAME)
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val playlistId: Int?=null,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(
        name = "imageUri",
        typeAffinity = ColumnInfo.TEXT,
        defaultValue = ""
    ) val imageUri: String = "",
)