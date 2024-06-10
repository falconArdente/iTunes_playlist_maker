package com.example.playlistmaker.media.model.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.media.model.data.db.dao.FavoriteTracksDao
import com.example.playlistmaker.media.model.data.db.dao.PlaylistsDao
import com.example.playlistmaker.media.model.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media.model.data.db.entity.PlaylistTrackEntity
import com.example.playlistmaker.media.model.data.db.entity.TrackEntity

@Database(
    version = 2,
    entities = [TrackEntity::class, PlaylistEntity::class, PlaylistTrackEntity::class]
)
abstract class AppDbRoomBased : RoomDatabase() {
    abstract fun favoriteTracksDao(): FavoriteTracksDao
    abstract fun playlistsDao(): PlaylistsDao
}