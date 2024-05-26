package com.example.playlistmaker.media.model.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.media.model.data.db.dao.FavoriteTracksDao
import com.example.playlistmaker.media.model.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDbRoomBased : RoomDatabase() {
    abstract fun favoriteTracksDao(): FavoriteTracksDao
}