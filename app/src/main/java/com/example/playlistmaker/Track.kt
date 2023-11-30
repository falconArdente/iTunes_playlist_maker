package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class Track(
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String
)

class TrackVHadapter(
    private val data: ArrayList<Track>
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

class TrackViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.track_row_at_search, parent, false)//built-in name of object is itemView
) {

    private val rootLayout: LinearLayout = itemView.findViewById(R.id.root_layout_track_row)
    private val image: ImageView = itemView.findViewById(R.id.image_track)
    private val trackTitle: TextView = itemView.findViewById(R.id.track_title)
    private val artistName: TextView = itemView.findViewById(R.id.artist_name)
    private val duration: TextView = itemView.findViewById(R.id.track_time)
    private val textHost:LinearLayout=itemView.findViewById(R.id.track_texts_host)
    fun bind(item: Track) {
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.image_mok)
            .fitCenter()
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.track_row_image_corners)))
            .into(image)
        trackTitle.text = item.trackName
        artistName.text = item.artistName
        duration.text = item.trackTime
        //toDo
        println(artistName.width.toString())
    }
}

