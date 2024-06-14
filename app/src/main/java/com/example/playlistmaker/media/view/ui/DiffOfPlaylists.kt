package com.example.playlistmaker.media.view.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.media.model.domain.Playlist

class DiffOfPlaylists(private val oldList: List<Playlist>, private val newList: List<Playlist>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        with(oldList[oldItemPosition]) {
            newList[newItemPosition].let { new ->
                if (this.title != new.title) return false
                if (this.imageUri != new.imageUri) return false
                if (this.tracksCount != new.tracksCount) return false
                if (this.tracks.hashCode() != new.tracks.hashCode()) return false
            }
        }
        return true
    }
}