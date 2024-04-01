package com.example.playlistmaker.media.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavoriteTrackListBinding
import com.example.playlistmaker.media.viewModel.FavoriteTracksFragmentViewModel
import com.example.playlistmaker.search.model.domain.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksListFragment : Fragment() {
    companion object {
        fun newInstance(): Fragment {
            return FavoriteTracksListFragment()
        }
    }

    private lateinit var binding: FragmentFavoriteTrackListBinding
    private val favoritesViewModel by viewModel<FavoriteTracksFragmentViewModel>()
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
        favoritesViewModel.observeFavoriteTracks().observe(viewLifecycleOwner) { render(it) }
    }

    private fun render(tracks: List<Track>) {
        val isNoTracksBool = (tracks.isEmpty())
        binding.placeholderFrame.isVisible = isNoTracksBool
        if (!isNoTracksBool) Log.d("Fragments", "favorites have ${tracks.size} tracks")
    }
}