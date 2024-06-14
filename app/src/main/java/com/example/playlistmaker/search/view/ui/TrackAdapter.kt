package com.example.playlistmaker.search.view.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackRowAtSearchBinding
import com.example.playlistmaker.search.model.domain.Track

class TrackAdapter(
    tracks: List<Track>, private val onClickListener: TrackOnClickListener
) : RecyclerView.Adapter<TrackViewHolder>() {

    var tracks = tracks
        set(value) {
            if (tracks != null) {
                val diffCallback = DiffForTrack(field, value)
                val difference = DiffUtil.calculateDiff(diffCallback)
                field = value
                difference.dispatchUpdatesTo(this)
                return
            }
            field = value
        }
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



