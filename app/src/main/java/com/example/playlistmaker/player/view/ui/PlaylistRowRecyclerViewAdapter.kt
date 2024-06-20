package com.example.playlistmaker.player.view.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistViewHolderRowBinding
import com.example.playlistmaker.media.model.domain.Playlist
import com.example.playlistmaker.media.view.ui.DiffOfPlaylists

class PlaylistRowRecyclerViewAdapter(
    playlists: List<Playlist>, private val onClickListener: PlaylistViewOnClickListener
) : RecyclerView.Adapter<PlaylistRowViewHolder>() {

    var playlists = playlists
        set(value) {
            if (playlists != null) {
                val diffCalback = DiffOfPlaylists(playlists, value)
                val difference = DiffUtil.calculateDiff(diffCalback)
                field = value
                difference.dispatchUpdatesTo(this)
                return
            }
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistRowViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistRowViewHolder(
            PlaylistViewHolderRowBinding.inflate(
                layoutInspector, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PlaylistRowViewHolder, position: Int) {
        holder.bind(playlists[position], onClickListener)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}



