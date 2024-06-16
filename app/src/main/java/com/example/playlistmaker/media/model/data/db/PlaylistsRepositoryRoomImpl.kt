package com.example.playlistmaker.media.model.data.db

import android.net.Uri
import androidx.core.net.toUri
import com.example.playlistmaker.media.model.data.db.dao.PlaylistsDao
import com.example.playlistmaker.media.model.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media.model.domain.Playlist
import com.example.playlistmaker.media.model.repository.PlaylistsRepository
import com.example.playlistmaker.search.model.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryRoomImpl(private val playlistsTable: PlaylistsDao) : PlaylistsRepository {

    override suspend fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistsTable.getPlaylistsWithCountOfTracks().map { listOfPlaylistWithCount ->
            listOfPlaylistWithCount.map { onePlaylistWithCount ->
                Playlist(
                    id = onePlaylistWithCount.playlistEntity.playlistId ?: -1,
                    title = onePlaylistWithCount.playlistEntity.title,
                    description = onePlaylistWithCount.playlistEntity.description ?: "",
                    imageUri = onePlaylistWithCount.playlistEntity.imageUri.toUri(),
                    tracks = emptyList(),
                    tracksCount = onePlaylistWithCount.tracksCount
                )
            }
        }
    }

    fun getAllPlaylistsTracksIncluded(): Flow<List<Playlist>> {
        return playlistsTable.getPlaylistsWithTracks().map { listOfPlaylistWithPtracks ->
            listOfPlaylistWithPtracks.map { onePlaylistWithPtracks ->
                Playlist(id = onePlaylistWithPtracks.playlistEntity.playlistId ?: -1,
                    title = onePlaylistWithPtracks.playlistEntity.title,
                    description = onePlaylistWithPtracks.playlistEntity.description ?: "",
                    imageUri = onePlaylistWithPtracks.playlistEntity.imageUri.toUri(),
                    tracks = onePlaylistWithPtracks.playlistTracks.map { trackEntity ->
                        TrackDbConverter.map(trackEntity)
                    })
            }
        }
    }

    override suspend fun createPlaylist(title: String, description: String, imageUri: Uri?) {
        return playlistsTable.createPlaylist(
            PlaylistEntity(
                title = title, description = description, imageUri = imageUri.toString()
            )
        )
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        return playlistsTable.getPlaylistsById(playlist.id).collect { list ->
            list.forEach { playlistEntity ->
                playlistsTable.deletePlaylistEntity(playlistEntity)
            }
        }
    }

    override fun addTrackToPlaylist(trackToAdd: Track, playlist: Playlist): Boolean {
        val idsOfTracks =
            playlistsTable.getTracksRemoteIDsByPlaylistStraight(playlistId = playlist.id)
        if (idsOfTracks.contains(trackToAdd.id)) return false //already have this track case
        playlistsTable.addPTrack(
            TrackDbConverter.map(trackToAdd, playlist)
        )
        return true
    }

    override suspend fun removeTrackFromPlaylist(trackToRemove: Track, playlist: Playlist) {
        playlistsTable.getTracksOfPlaylist(playlist.id).collect { pTrackList ->
            pTrackList.forEach { pTrackEntity ->
                playlistsTable.removePTrack(pTrackEntity)
            }
        }
    }

    override suspend fun getTracksOfPlaylist(playlist: Playlist): Flow<List<Track>> {
        return playlistsTable.getTracksOfPlaylist(playlist.id).map { listOfEntity ->
            listOfEntity.map { pTrackEntity ->
                TrackDbConverter.map(pTrackEntity)
            }
        }
    }

    override suspend fun getPlaylistWithTracksById(playlistId: Int): Flow<Playlist> {
        return playlistsTable.getPlaylistWithTracksById(playlistId)
            .map { playlistAndTracksEntities ->
                Playlist(id = playlistAndTracksEntities.playlistEntity.playlistId ?: 0,
                    title = playlistAndTracksEntities.playlistEntity.title,
                    description = playlistAndTracksEntities.playlistEntity.description ?: "",
                    imageUri = playlistAndTracksEntities.playlistEntity.imageUri.toUri(),
                    tracks = playlistAndTracksEntities.playlistTracks.map { TrackDbConverter.map(it) })
            }
    }

    override suspend fun getPlaylistById(playlistId: Int): Playlist {
        return playlistsTable.getFirstPlaylistById(playlistId)
            .let { playlistEntity ->
                Playlist(
                    id = playlistEntity.playlistId ?: 0,
                    title = playlistEntity.title,
                    description = playlistEntity.description ?: "",
                    imageUri = playlistEntity.imageUri.toUri(),
                    tracks = emptyList()
                )
            }
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistsTable.updatePlaylist(TrackDbConverter.map(playlist))
    }
}