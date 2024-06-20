package com.example.playlistmaker.media.view.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistViewHolderGridBinding
import com.example.playlistmaker.media.model.domain.Playlist

class PlaylistGridAdapter(
    playlists: List<Playlist>, private val playlistOnClickListener: PlaylistOnClickListener
) : RecyclerView.Adapter<PlaylistViewHolder>() {

    var playlists = playlists
        set(value) {
            if (playlists != null) {
                val diffCallback = DiffOfPlaylists(playlists, value)
                val difference = DiffUtil.calculateDiff(diffCallback)
                field = value
                difference.dispatchUpdatesTo(this)
                return
            }
            field = value
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(
            PlaylistViewHolderGridBinding.inflate(
                layoutInspector,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position], playlistOnClickListener)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}



