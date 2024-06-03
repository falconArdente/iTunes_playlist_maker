package com.example.playlistmaker.media.model.data.db.entity

import androidx.room.Embedded
import androidx.room.Relation

data class PlaylistsWithTracks(
    @Embedded val playlistEntity: PlaylistEntity,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "playlistId"
    )
    val playlistTracks:List<PlaylistTrackEntity>
)
