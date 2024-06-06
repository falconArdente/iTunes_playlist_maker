package com.example.playlistmaker.media.view

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistViewHolderBinding
import com.example.playlistmaker.media.model.domain.Playlist

class PlaylistViewHolder(private val binding: PlaylistViewHolderBinding) : RecyclerView.ViewHolder(
    binding.root
) {
    fun bind(item: Playlist) {
        binding.playlistTitle.text = item.title
        val count=item.tracks.size
        binding.playlistTracksCount.text = "$count "+
            itemView.resources.getQuantityString(R.plurals.tracks_count, count)
        Glide.with(itemView)
            .load(item.imageUri)
            .placeholder(R.drawable.placeholder_search_bar)
            .fitCenter()
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.create_playlist_image_corner_radius)))
            .into(binding.playlistThumbnail)
    }
}