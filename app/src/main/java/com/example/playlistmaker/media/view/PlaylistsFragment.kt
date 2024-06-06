package com.example.playlistmaker.media.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.media.viewModel.PlaylistScreenState
import com.example.playlistmaker.media.viewModel.PlaylistsFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    companion object {
        fun newInstance(): Fragment {
            return PlaylistsFragment()
        }

        const val COLUMNS_COUNT = 2
    }

    private lateinit var binding: FragmentPlaylistsBinding
    private val playlistsViewModel by viewModel<PlaylistsFragmentViewModel>()
    private val playlistsAdapter = PlaylistAdapter(emptyList())
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistsViewModel.screenStateToObserve.observe(viewLifecycleOwner) { render(screenState = it) }
        binding.playlistsRecyclerView.adapter = playlistsAdapter
        binding.playlistsRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), COLUMNS_COUNT, GridLayoutManager.VERTICAL, false)
        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_createPlaylistFragment)
        }
    }

    private fun render(screenState: PlaylistScreenState) {
        when (screenState) {
            is PlaylistScreenState.Empty -> {
                binding.placeholderFrame.isVisible = true
                binding.playlistsRecyclerView.isVisible = false
            }

            is PlaylistScreenState.HaveData -> {
                binding.placeholderFrame.isVisible = false
                playlistsAdapter.playlists = screenState.playlists
                playlistsAdapter.notifyDataSetChanged()
                binding.playlistsRecyclerView.isVisible = true
            }
        }
    }
}