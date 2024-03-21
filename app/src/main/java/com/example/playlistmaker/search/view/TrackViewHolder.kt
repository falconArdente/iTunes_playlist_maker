package com.example.playlistmaker.search.view

import android.icu.text.SimpleDateFormat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackRowAtSearchBinding
import com.example.playlistmaker.search.model.domain.Track
import java.util.Locale

class TrackViewHolder(private val binding: TrackRowAtSearchBinding) : RecyclerView.ViewHolder(
    binding.root//built-in name of object is itemView
) {
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    fun bind(item: Track, onClickListener: TrackOnClickListener) {
        binding.trackTitle.text = item.trackTitle
        binding.trackTime.text =
            dateFormat.format(item.duration.toLong())
        binding.artistName.text = item.artistName
        Glide.with(itemView)
            .load(item.artwork)
            .placeholder(R.drawable.placeholder_search_bar)
            .fitCenter()
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.track_row_image_corners)))
            .into(binding.imageTrack)
        itemView.setOnClickListener { onClickListener.onClick(item) }
    }
}