package com.example.playlistmaker.media.view

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistItemViewBinding
import com.example.playlistmaker.databinding.PlaylistItemBottomSheetBinding
import com.example.playlistmaker.media.viewModel.PlaylistItemScreenState
import com.example.playlistmaker.media.viewModel.PlaylistItemViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt

class PlaylistItemFragment : Fragment() {

    companion object {
        private const val PLAYLIST_ID = "playlist"
        fun createArgs(playlistId: Int): Bundle {
            return bundleOf(PLAYLIST_ID to playlistId)
        }
    }

    private val viewModel by viewModel<PlaylistItemViewModel>()
    private var binding: FragmentPlaylistItemViewBinding? = null
    private var bottomSheetBinding: PlaylistItemBottomSheetBinding? = null
    private var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null

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
        bottomSheetBehavior =
            BottomSheetBehavior.from(bottomSheetBinding!!.bottomSheet)
        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    @SuppressLint("SetTextI18n")
    private fun render(state: PlaylistItemScreenState) {
        if (bottomSheetBehavior!!.peekHeight == BottomSheetBehavior.PEEK_HEIGHT_AUTO) {
            with(binding!!) {
                bottomSheetBehavior!!.peekHeight =
                    (root.height - shareIcon.y - shareIcon.height - requireContext().resources.getDimensionPixelSize(
                        R.dimen.playlist_icon_size
                    )).roundToInt()
            }
        }
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
        }
    }
}