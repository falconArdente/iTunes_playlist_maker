package com.example.playlistmaker

import android.icu.text.SimpleDateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.domain.models.Track
import java.util.Locale

interface TrackOnClickListener {
    fun onClick(item: Track)
}

class TrackAdapter(
    var tracks: List<Track>, private val onClickListener: TrackOnClickListener
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent).apply { Log.d("history", "Get inside VH") }

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
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    fun bind(item: Track, onClickListener: TrackOnClickListener) {
        Log.d("VH", "binding $item")
        itemView.findViewById<TextView>(R.id.track_title).text = item.trackTitle
        Log.d("VH", "binding time ${item.duration}")
        itemView.findViewById<TextView>(R.id.track_time).text =
            dateFormat.format(item.duration.toLong())
        itemView.findViewById<TextView>(R.id.artist_name).text = item.artistName
        Log.d("VH", "binding art")
        Glide.with(itemView)
            .load(item.artwork)
            .placeholder(R.drawable.placeholder_search_bar)
            .fitCenter()
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.track_row_image_corners)))
            .into(itemView.findViewById(R.id.image_track))
        itemView.setOnClickListener { onClickListener.onClick(item) }
        Log.d("VH", "done binding")
    }
}
