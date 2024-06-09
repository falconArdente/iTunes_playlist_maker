package com.example.playlistmaker.media.view.ui

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistViewHolderGridBinding
import com.example.playlistmaker.media.model.domain.Playlist

class PlaylistViewHolder(private val binding: PlaylistViewHolderGridBinding) :
    RecyclerView.ViewHolder(
        binding.root
    ) {
    @SuppressLint("SetTextI18n")
    fun bind(item: Playlist) {
        binding.playlistTitle.text = item.title
        val count = getCount(item)
        binding.playlistTracksCount.text =
            "$count ${itemView.resources.getQuantityString(R.plurals.tracks_count, count)}"
        Glide.with(itemView).load(item.imageUri).placeholder(R.drawable.placeholder_search_bar)
            .fitCenter().transform(
                CenterCrop(),
                RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.create_playlist_image_corner_radius))
            ).into(binding.playlistThumbnail)
    }

    private fun getCount(playlist: Playlist): Int {
        if (playlist.tracksCount != null) return playlist.tracksCount
        return playlist.tracks.size
    }
}