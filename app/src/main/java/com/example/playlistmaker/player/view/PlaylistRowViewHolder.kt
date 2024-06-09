package com.example.playlistmaker.player.view

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistViewHolderRowBinding
import com.example.playlistmaker.media.model.domain.Playlist

class PlaylistRowViewHolder(private val binding: PlaylistViewHolderRowBinding) :
    RecyclerView.ViewHolder(
        binding.root
    ) {
    @SuppressLint("SetTextI18n")
    fun bind(item: Playlist, onClickListener: PlaylistViewOnClickListener) {
        binding.playlistTitle.text = item.title
        val count = getCount(item)
        binding.playlistTracksCount.text =
            "$count ${itemView.resources.getQuantityString(R.plurals.tracks_count, count)}"
        Glide.with(itemView).load(item.imageUri).placeholder(R.drawable.placeholder_search_bar)
            .fitCenter().centerCrop().into(binding.playlistThumbnail)
        itemView.setOnClickListener { onClickListener.onClick(item) }
    }

    private fun getCount(playlist: Playlist): Int {
        return playlist.tracksCount ?: playlist.tracks.size
    }
}