package com.example.playlistmaker.media.view

import android.icu.text.SimpleDateFormat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackRowAtFavoritesBinding
import com.example.playlistmaker.search.model.domain.Track
import java.util.Locale

class FavoriteTrackViewHolder(private val binding: TrackRowAtFavoritesBinding) :
    RecyclerView.ViewHolder(
        binding.root
    ) {
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    fun bind(item: Track, onClickListener: FavoriteTrackOnClickListener) {
        binding.trackTitle.text = item.trackTitle
        binding.trackTime.text = dateFormat.format(item.duration.toLong())
        binding.artistName.text = item.artistName
        Glide.with(itemView).load(item.artwork).placeholder(R.drawable.placeholder_media_bar)
            .fitCenter().centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.track_row_image_corners)))
            .into(binding.imageTrack)
        itemView.setOnClickListener { onClickListener.onClick(item) }
    }
}