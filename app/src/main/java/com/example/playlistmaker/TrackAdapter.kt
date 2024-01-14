package com.example.playlistmaker

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.util.Locale

private const val TRACK_KEY = "track"

class TrackAdapter(
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

    public fun onClickListener() {}
}

class TrackViewHolder(val parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.track_row_at_search, parent, false)//built-in name of object is itemView
) {
    private val image: ImageView = itemView.findViewById(R.id.image_track)
    private val trackTitle: TextView = itemView.findViewById(R.id.track_title)
    private val artistName: TextView = itemView.findViewById(R.id.artist_name)
    private val duration: TextView = itemView.findViewById(R.id.track_time)
    private val rootLayout: LinearLayout = itemView.findViewById(R.id.root_layout_track_row)
    fun bind(item: Track) {
        duration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.duration.toLong())
        trackTitle.text = item.trackName
        artistName.text = item.artistName
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.placeholder_search_bar)
            .fitCenter()
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.track_row_image_corners)))
            .into(image)
        rootLayout.setOnClickListener {
            (parent.context.applicationContext as App).history.addTrack(item)
            val intent = Intent(parent.context, PlayerActivity::class.java)
            val json = Gson()
            intent.putExtra(TRACK_KEY, json.toJson(item))
            Log.d(TRACK_KEY, intent.toString())
            startActivity(parent.context, intent, null)
        }
    }
}
