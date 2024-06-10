package com.example.playlistmaker.media.model.data.db.entity

import androidx.room.Embedded
import androidx.room.Relation

class PlaylistsWithTracks(
    @Embedded val playlistEntity: PlaylistEntity,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "playlistRelationId",
    ) val playlistTracks: List<PlaylistTrackEntity>
)