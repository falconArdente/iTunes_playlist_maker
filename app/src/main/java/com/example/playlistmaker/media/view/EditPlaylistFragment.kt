package com.example.playlistmaker.media.view

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.example.playlistmaker.R
import com.example.playlistmaker.media.viewModel.EditPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : CreatePlaylistFragment() {
    companion object {
        private const val PLAYLIST_ID = "playlist"
        fun createArgs(playlistId: Int): Bundle {
            return bundleOf(PLAYLIST_ID to playlistId)
        }
    }
     override val viewModel by viewModel<EditPlaylistViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (binding!=null){
            with(binding!!){
                header.title = getString(R.string.create_playlist_edit_caption)
                createButton.text = getString(R.string.create_playlist_update_button)
                playlistImage.setBackgroundDrawable(requireContext().getDrawable(R.drawable.placeholder_media_bar))
            }
        }
        val playlistId = arguments?.getInt(PLAYLIST_ID, -1)
        if (playlistId != null && playlistId != -1) viewModel.setPlaylistToEdit(playlistId)
    }
}