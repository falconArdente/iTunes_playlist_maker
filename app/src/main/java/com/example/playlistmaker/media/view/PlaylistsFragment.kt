package com.example.playlistmaker.media.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.media.model.domain.Playlist
import com.example.playlistmaker.media.viewModel.PlaylistsFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    companion object {
        fun newInstance(): Fragment {
            return PlaylistsFragment()
        }
    }

    private lateinit var binding: FragmentPlaylistsBinding
    private val playlistsViewModel by viewModel<PlaylistsFragmentViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistsViewModel.observePlaylists().observe(viewLifecycleOwner) { render(it) }
    }

    private fun render(playlists: List<Playlist>) {
        val isNoPlaylistsBool = (playlists.isEmpty())
        binding.placeholderFrame.isVisible = isNoPlaylistsBool
        binding.newPlaylistButton.isVisible = isNoPlaylistsBool
    }
}