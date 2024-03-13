package com.example.playlistmaker.search.ui

import android.icu.text.SimpleDateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackRowAtSearchBinding
import com.example.playlistmaker.search.domain.Track
import java.util.Locale

interface TrackOnClickListener {
    fun onClick(item: Track)
}

class TrackAdapter(
    var tracks: List<Track>, private val onClickListener: TrackOnClickListener
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(TrackRowAtSearchBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position], onClickListener)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}

class TrackViewHolder(private val binding: TrackRowAtSearchBinding) : RecyclerView.ViewHolder(
    binding.root//built-in name of object is itemView
) {
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    fun bind(item: Track, onClickListener: TrackOnClickListener) {
        Log.d("VH", "binding $item")
        binding.trackTitle.text = item.trackTitle
        Log.d("VH", "binding time ${item.duration}")
        binding.trackTime.text =
            dateFormat.format(item.duration.toLong())
        binding.artistName.text = item.artistName
        Log.d("VH", "binding art")
        Glide.with(itemView)
            .load(item.artwork)
            .placeholder(R.drawable.placeholder_search_bar)
            .fitCenter()
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.track_row_image_corners)))
            .into(binding.imageTrack)
        itemView.setOnClickListener { onClickListener.onClick(item) }
        Log.d("VH", "done binding")
    }
}
