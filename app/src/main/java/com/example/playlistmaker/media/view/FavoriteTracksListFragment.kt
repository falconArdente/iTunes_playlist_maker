package com.example.playlistmaker.media.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentFavoriteTrackListBinding
import com.example.playlistmaker.media.viewModel.FavoriteTracksFragmentViewModel
import com.example.playlistmaker.media.viewModel.FavoriteTracksScreenState
import com.example.playlistmaker.search.model.domain.Track
import com.example.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksListFragment : Fragment() {
    companion object {
        private const val CHOICE_DEBOUNCE_DELAY = 1100L
        fun newInstance(): Fragment {
            return FavoriteTracksListFragment()
        }
    }

    private lateinit var binding: FragmentFavoriteTrackListBinding
    private val favoritesViewModel by viewModel<FavoriteTracksFragmentViewModel>()
    private var favoriteTracksAdapter: FavoriteTracksAdapter? = null
    private lateinit var trackOnClickDebounced: (Track) -> Unit
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteTrackListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trackOnClickDebounced = debounce(
            CHOICE_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            favoritesViewModel.openTrack(track)
        }
        favoriteTracksAdapter = FavoriteTracksAdapter(emptyList(), trackOnClickListener)
        binding.favTracksRecyclerView.adapter = favoriteTracksAdapter
        binding.favTracksRecyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, true)
        favoritesViewModel.screenState.observe(viewLifecycleOwner) { render(screenState = it) }
    }

    private fun render(screenState: FavoriteTracksScreenState) {
        when (screenState) {
            is FavoriteTracksScreenState.NoTracks -> {
                binding.favTracksRecyclerView.isVisible = false
                binding.placeholderFrame.isVisible = true
            }

            is FavoriteTracksScreenState.HaveTracks -> {
                favoriteTracksAdapter?.tracks = (screenState.tracks)
                favoriteTracksAdapter?.notifyDataSetChanged()
                binding.placeholderFrame.isVisible = false
                binding.favTracksRecyclerView.isVisible = true
            }
        }
    }

    private var trackOnClickListener = object : FavoriteTrackOnClickListener {
        override fun onClick(item: Track) {
            trackOnClickDebounced(item)
        }
    }
}