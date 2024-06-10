package com.example.playlistmaker.media.model.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded

class PlaylistsWithCount(
    @Embedded
    val playlistEntity: PlaylistEntity,
    @ColumnInfo(name = "child_count")
    val tracksCount: Int
)