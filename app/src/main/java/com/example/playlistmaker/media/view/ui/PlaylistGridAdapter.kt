package com.example.playlistmaker.media.view.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistViewHolderGridBinding
import com.example.playlistmaker.media.model.domain.Playlist

class PlaylistGridAdapter(
    var playlists: List<Playlist>, private val playlistOnClickListener: PlaylistOnClickListener
) : RecyclerView.Adapter<PlaylistViewHolder>() {

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



