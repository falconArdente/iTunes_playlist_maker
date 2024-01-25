package com.example.playlistmaker

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.TrackRowAtSearchBinding
import java.util.Locale

interface TrackOnClickListener {
    fun onClick(item: Track)
}

class TrackAdapter(
    var tracks: ArrayList<Track>, private val onClickListener: TrackOnClickListener
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position], onClickListener)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}

class TrackViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.track_row_at_search, parent, false)//built-in name of object is itemView
) {
    init {
        TrackRowAtSearchBinding.inflate(LayoutInflater.from(parent.context))
            .also { this.binding = it }
    }
    private var binding: TrackRowAtSearchBinding
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    fun bind(item: Track, onClickListener: TrackOnClickListener) {
        binding.trackTime.text = dateFormat.format(item.duration.toLong())
        binding.trackTitle.text = item.trackName
        binding.artistName.text = item.artistName
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.placeholder_search_bar)
            .fitCenter()
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.track_row_image_corners)))
            .into(itemView.findViewById(R.id.image_track))
        itemView.setOnClickListener { onClickListener.onClick(item) }
    }
}
