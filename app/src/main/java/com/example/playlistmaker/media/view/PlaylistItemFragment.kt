package com.example.playlistmaker.media.view

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistItemViewBinding
import com.example.playlistmaker.databinding.PlaylistItemBottomSheetBinding
import com.example.playlistmaker.media.viewModel.PlaylistItemScreenState
import com.example.playlistmaker.media.viewModel.PlaylistItemViewModel
import com.example.playlistmaker.search.view.ui.DiffForTrack
import com.example.playlistmaker.search.view.ui.TrackAdapter
import com.example.playlistmaker.search.view.ui.TrackOnClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt

class PlaylistItemFragment : Fragment() {

    companion object {
        private const val PLAYLIST_ID = "playlist"
        private const val BOOTTOMSHEET_PEEK_RETRY_DELAY= 10L
        private const val BOOTTOMSHEET_PEEK_TRY_COUNT = 10
        fun createArgs(playlistId: Int): Bundle {
            return bundleOf(PLAYLIST_ID to playlistId)
        }
    }

    private val viewModel by viewModel<PlaylistItemViewModel>()
    private var binding: FragmentPlaylistItemViewBinding? = null
    private var bottomSheetBinding: PlaylistItemBottomSheetBinding? = null
    private var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null

    private val trackClick = TrackOnClickListener {
        viewModel.goToPlay(it)
    }
    private val adapter: TrackAdapter = TrackAdapter(emptyList(), trackClick)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel.setPlaylistById(requireArguments().getInt(PLAYLIST_ID))
        binding = FragmentPlaylistItemViewBinding.inflate(inflater, container, false)
        bottomSheetBinding = PlaylistItemBottomSheetBinding.inflate(inflater, binding!!.root, true)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.playlistScreenToObserve.observe(viewLifecycleOwner) { render(it) }
        if (resources.configuration.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            bottomSheetBinding!!.recyclerViewPlaylistItem.layoutManager =
                LinearLayoutManager(requireContext())
        } else {
            bottomSheetBinding!!.recyclerViewPlaylistItem.layoutManager =
                GridLayoutManager(requireContext(), 2)
        }
        bottomSheetBinding!!.recyclerViewPlaylistItem.adapter = adapter
        bottomSheetBehavior =
            BottomSheetBehavior.from(bottomSheetBinding!!.bottomSheet)
        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        binding?.header!!.setNavigationOnClickListener { viewModel.goBack(this) }
    }

    override fun onResume() {
        super.onResume()
        if (bottomSheetBehavior!!.peekHeight != BottomSheetBehavior.PEEK_HEIGHT_AUTO) return
        lifecycleScope.launch {
            val height = async { getHeightPickValue() }
            bottomSheetBehavior!!.peekHeight = height.await()
        }
    }

    private suspend fun getHeightPickValue(): Int {
        var value = 0
        var leftToTry = BOOTTOMSHEET_PEEK_TRY_COUNT
        var isFirstTry = true
        if (binding != null) {
            while (value < 0 || leftToTry > 0) {
                if (!isFirstTry) delay(BOOTTOMSHEET_PEEK_RETRY_DELAY)
                with(binding!!) {
                    value =
                        (root.height - shareIcon.y - shareIcon.height - requireContext().resources.getDimensionPixelSize(
                            R.dimen.playlist_icon_size
                        )).roundToInt()
                }
                leftToTry -= 1
                isFirstTry = false
            }
        }
        return if (value > 0) value else BottomSheetBehavior.PEEK_HEIGHT_AUTO
    }

    @SuppressLint("SetTextI18n")
    private fun render(state: PlaylistItemScreenState) {
        if (state is PlaylistItemScreenState.Empty) return
        with(state as PlaylistItemScreenState.HaveData) {
            if (this.imageUri != Uri.EMPTY) {
                Glide
                    .with(this@PlaylistItemFragment)
                    .load(this.imageUri)
                    .placeholder(R.drawable.placeholder_media_bar)
                    .into(binding!!.playlistImage)
            }
            binding!!.let { binding ->
                binding.titlePlt.text = title
                binding.description.text = description
                binding.minutesPlt.text =
                    "$minutes ${resources.getQuantityString(R.plurals.minutes_count, minutes)}"
                binding.tracks.text =
                    "$tracksCount ${
                        resources.getQuantityString(
                            R.plurals.tracks_count, tracksCount
                        )
                    }"
            }
            val trackUtilCallback = DiffForTrack(adapter.tracks, state.trackList)
            val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(trackUtilCallback)
            adapter.tracks = state.trackList
            diffResult.dispatchUpdatesTo(adapter)
        }
    }
}