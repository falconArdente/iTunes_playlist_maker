package com.example.playlistmaker.media.model.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media.model.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

private const val TABLE_NAME = "favorite_tracks_table"

@Dao
interface FavoriteTracksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrackEntity(trackEntity: TrackEntity)

    @Delete(TrackEntity::class)
    suspend fun deleteTrackEntity(trackEntity: TrackEntity): Int

    @Query("SELECT * FROM $TABLE_NAME")
    fun getAll(): Flow<List<TrackEntity>>

    @Query("SELECT remoteId FROM $TABLE_NAME")
    fun getRemoteIdList(): Flow<List<Long>>
    @Query("SELECT * FROM $TABLE_NAME WHERE remoteId=:remoteID")
    fun getEntityListByRemoteId(remoteID:Long): List<TrackEntity>
}