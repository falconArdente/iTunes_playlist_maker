package com.example.playlistmaker.media.model.data.db

import com.example.playlistmaker.media.model.data.db.entity.TrackEntity
import com.example.playlistmaker.search.model.domain.Track

object TrackDbConverter {

    fun map(track: Track) = TrackEntity(
        remoteId = track.id,
        trackName = track.trackTitle,
        artistName = track.artistName,
        trackTimeMillis = track.duration,
        artworkUrl100 = track.artwork,
        collectionName = track.collectionName,
        releaseDate = track.releaseDate,
        genre = track.genre,
        country = track.country,
        previewUrl = track.trackPreview
    )

    fun map(trackEntity: TrackEntity) = Track(
        id = trackEntity.remoteId,
        trackTitle = trackEntity.trackName,
        artistName = trackEntity.artistName,
        duration = trackEntity.trackTimeMillis,
        artwork = trackEntity.artworkUrl100,
        collectionName = trackEntity.collectionName,
        releaseDate = trackEntity.releaseDate,
        genre = trackEntity.genre,
        country = trackEntity.country,
        trackPreview = trackEntity.previewUrl
    )
}