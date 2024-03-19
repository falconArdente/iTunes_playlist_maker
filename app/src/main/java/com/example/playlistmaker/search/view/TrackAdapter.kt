package com.example.playlistmaker.search.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackRowAtSearchBinding
import com.example.playlistmaker.search.model.domain.Track

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



