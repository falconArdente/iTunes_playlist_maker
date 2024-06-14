package com.example.playlistmaker.search.view.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.search.model.domain.Track

class DiffForTrack(private val oldTracks: List<Track>, private val newTracks: List<Track>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldTracks.size
    }

    override fun getNewListSize(): Int {
        return newTracks.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTracks[oldItemPosition].id == newTracks[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        with(oldTracks[oldItemPosition]) {
            newTracks[newItemPosition].let { new ->
                if (this.trackTitle != new.trackTitle) return false
                if (this.artwork != new.artwork) return false
                if (this.duration != new.duration) return false
                if (this.artistName != new.artistName) return false
            }
        }
        return true
    }
}