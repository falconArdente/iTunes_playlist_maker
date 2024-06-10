package com.example.playlistmaker.media.model.data.db

import com.example.playlistmaker.media.model.data.db.entity.PlaylistTrackEntity
import com.example.playlistmaker.media.model.data.db.entity.TrackEntity
import com.example.playlistmaker.media.model.domain.Playlist
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
    fun map(track: Track,playlist: Playlist) = PlaylistTrackEntity(
        playlistRelationId = playlist.id,
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
    fun map(pTrackEntity: PlaylistTrackEntity) = Track(
        id = pTrackEntity.remoteId,
        trackTitle = pTrackEntity.trackName,
        artistName = pTrackEntity.artistName,
        duration = pTrackEntity.trackTimeMillis,
        artwork = pTrackEntity.artworkUrl100,
        collectionName = pTrackEntity.collectionName,
        releaseDate = pTrackEntity.releaseDate,
        genre = pTrackEntity.genre,
        country = pTrackEntity.country,
        trackPreview = pTrackEntity.previewUrl
    )
}