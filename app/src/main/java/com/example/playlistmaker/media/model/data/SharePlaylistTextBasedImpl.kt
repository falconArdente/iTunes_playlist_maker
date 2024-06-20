package com.example.playlistmaker.media.model.data

import android.content.Context
import android.icu.text.SimpleDateFormat
import com.example.playlistmaker.R
import com.example.playlistmaker.media.model.domain.Playlist
import com.example.playlistmaker.media.model.domain.SharePlaylistUseCase
import com.example.playlistmaker.media.model.repository.ShareTextRepository
import java.util.Locale

class SharePlaylistTextBasedImpl(
    private val repository: ShareTextRepository,
    private val appContext: Context
) :
    SharePlaylistUseCase {
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    override fun execute(playlistToShare: Playlist) {
        repository.execute(
            getTextFromPlaylist(playlistToShare)
        )
    }

    private fun getTextFromPlaylist(playlist: Playlist): String {
        val tracksCountString = "${playlist.tracks.size} ${
            appContext.resources.getQuantityString(
                R.plurals.tracks_count,
                playlist.tracks.size
            )
        }"
        var trackListString = ""
        playlist.tracks.forEachIndexed { index, track ->
            trackListString += "${index + 1}. ${track.artistName} - ${track.trackTitle} " +
                    "(${dateFormat.format(track.duration.toLong())}) \n"
        }
        return "${playlist.title}\n" +
                "${playlist.description}\n" +
                "$tracksCountString \n" +
                trackListString
    }
}