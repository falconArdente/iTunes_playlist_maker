package com.example.playlistmaker.media.model.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.playlistmaker.media.model.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media.model.data.db.entity.PlaylistTrackEntity
import com.example.playlistmaker.media.model.data.db.entity.PlaylistsWithTracks
import kotlinx.coroutines.flow.Flow

private const val TABLE_NAME = "playlists_table"
private const val TRACKS_TABLE_NAME = "playlists_tracks_table"

@Dao
interface PlaylistsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createPlaylist(playlist: PlaylistEntity)

    @Delete(PlaylistEntity::class)
    suspend fun deletePlaylistEntity(playlist: PlaylistEntity): Int

    @Query("SELECT * FROM $TABLE_NAME")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM $TABLE_NAME WHERE playlistId=:playlistId")
    fun getPlaylistById(playlistId: Int): Flow<List<PlaylistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPTrack(playlistTrackEntity: PlaylistTrackEntity)

    @Delete(PlaylistTrackEntity::class)
    suspend fun removePTrack(playlistTrackEntity: PlaylistTrackEntity): Int

    @Query("SELECT * FROM $TRACKS_TABLE_NAME WHERE playlistId=:playlistId")
    fun getTracksOfPlaylist(playlistId: Int): Flow<List<PlaylistTrackEntity>>
    @Query("SELECT * FROM $TRACKS_TABLE_NAME WHERE playlistId=:playlistId")
    fun getTracksOfPlaylistStraight(playlistId: Int): List<PlaylistTrackEntity>

    @Transaction
    @Query("SELECT * FROM $TRACKS_TABLE_NAME, $TABLE_NAME")
    fun getPlaylistsWithTracks(): Flow<List<PlaylistsWithTracks>>
}