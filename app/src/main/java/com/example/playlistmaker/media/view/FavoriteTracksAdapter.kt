package com.example.playlistmaker.media.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackRowAtFavoritesBinding
import com.example.playlistmaker.search.model.domain.Track

class FavoriteTracksAdapter(
    var tracks: List<Track>, private val onClickListener: FavoriteTrackOnClickListener
) : RecyclerView.Adapter<FavoriteTrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteTrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return FavoriteTrackViewHolder(TrackRowAtFavoritesBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: FavoriteTrackViewHolder, position: Int) {
        holder.bind(tracks[position], onClickListener)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}



