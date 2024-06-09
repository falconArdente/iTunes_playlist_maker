package com.example.playlistmaker.player.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistViewHolderRowBinding
import com.example.playlistmaker.media.model.domain.Playlist

class PlaylistRowRecyclerViewAdapter(
    var playlists: List<Playlist>, private val onClickListener: PlaylistViewOnClickListener
) : RecyclerView.Adapter<PlaylistRowViewHolder>() {

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



