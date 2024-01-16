package com.example.playlistmaker

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
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
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private val image: ImageView = itemView.findViewById(R.id.image_track)
    private val trackTitle: TextView = itemView.findViewById(R.id.track_title)
    private val artistName: TextView = itemView.findViewById(R.id.artist_name)
    private val duration: TextView = itemView.findViewById(R.id.track_time)
    private val rootLayout: LinearLayout = itemView.findViewById(R.id.root_layout_track_row)
    fun bind(item: Track, onClickListener: TrackOnClickListener) {
        duration.text =dateFormat.format(item.duration.toLong())
        trackTitle.text = item.trackName
        artistName.text = item.artistName
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.placeholder_search_bar)
            .fitCenter()
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.track_row_image_corners)))
            .into(image)
        rootLayout.setOnClickListener { onClickListener.onClick(item) }
    }
}
