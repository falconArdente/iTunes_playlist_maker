package com.example.playlistmaker.media.view

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistItemViewBinding
import com.example.playlistmaker.databinding.PlaylistItemBottomSheetBinding
import com.example.playlistmaker.databinding.PlaylistItemOptionsSheetBinding
import com.example.playlistmaker.media.view.ui.CanShowPlaylistMessage
import com.example.playlistmaker.media.view.ui.FragmentCanShowDialog
import com.example.playlistmaker.media.view.ui.PlaylistMessage
import com.example.playlistmaker.media.viewModel.PlaylistItemScreenState
import com.example.playlistmaker.media.viewModel.PlaylistItemViewModel
import com.example.playlistmaker.player.view.ui.PlaylistRowViewHolder
import com.example.playlistmaker.search.view.ui.DiffForTrack
import com.example.playlistmaker.search.view.ui.TrackAdapter
import com.example.playlistmaker.search.view.ui.TrackOnClickListener
import com.example.playlistmaker.search.view.ui.TrackOnLongClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt

class PlaylistItemFragment : Fragment(), FragmentCanShowDialog, CanShowPlaylistMessage {

    companion object {
        private const val PLAYLIST_ID = "playlist"
        private const val BOOTTOMSHEET_PEEK_RETRY_DELAY = 10L
        private const val BOOTTOMSHEET_PEEK_TRY_COUNT = 10
        private const val FADE_DURATION = 250L
        fun createArgs(playlistId: Int): Bundle {
            return bundleOf(PLAYLIST_ID to playlistId)
        }
    }

    private val viewModel by viewModel<PlaylistItemViewModel>()
    private var binding: FragmentPlaylistItemViewBinding? = null
    private var tracksSheetBinding: PlaylistItemBottomSheetBinding? = null
    private var tracksSheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null
    private var optionsSheetBinding: PlaylistItemOptionsSheetBinding? = null
    private var optionsSheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null

    private val trackClick = TrackOnClickListener {
        viewModel.goToPlay(it)
    }
    private val trackLongClick = TrackOnLongClickListener {
        viewModel.deleteTrackSequence(it)
    }
    private val adapter: TrackAdapter = TrackAdapter(emptyList(), trackClick, trackLongClick)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel.setPlaylistById(requireArguments().getInt(PLAYLIST_ID))
        viewModel.attachFragmentAtCreation(this)
        binding = FragmentPlaylistItemViewBinding.inflate(inflater, container, false)
        tracksSheetBinding = PlaylistItemBottomSheetBinding.inflate(inflater, binding!!.root, true)
        optionsSheetBinding =
            PlaylistItemOptionsSheetBinding.inflate(layoutInflater, binding!!.root, true)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.playlistScreenToObserve.observe(viewLifecycleOwner) { render(it) }
        if (tracksSheetBinding != null) {
            with(tracksSheetBinding!!) {
                if (resources.configuration.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    recyclerViewPlaylistItem.layoutManager =
                        LinearLayoutManager(requireContext())
                } else {
                    recyclerViewPlaylistItem.layoutManager =
                        GridLayoutManager(requireContext(), 2)
                }
                recyclerViewPlaylistItem.adapter = adapter
                tracksSheetBehavior =
                    BottomSheetBehavior.from(bottomSheet)
            }
        }
        if (optionsSheetBinding != null) {
            with(optionsSheetBinding!!) {
                this.share.setOnClickListener { viewModel.sharePlaylist() }
                this.edit.setOnClickListener { viewModel.editPlaylist() }
                this.delete.setOnClickListener { viewModel.deletePlaylistSequence() }
                optionsSheetBehavior =
                    BottomSheetBehavior.from(this.root)
            }
            optionsSheetBehavior?.addBottomSheetCallback(optionsSheetStateChangedCallback)
            optionsSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        }

        if (binding != null) {
            with(binding!!) {
                header.setNavigationOnClickListener { viewModel.goBack(this@PlaylistItemFragment) }
                shareIcon.setOnClickListener { viewModel.sharePlaylist() }
                optionsIcon.setOnClickListener { viewModel.showOptions(true) }
            }
        }
        optionsSheetBehavior?.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (binding == null) return
                val fadeOut = AlphaAnimation(0.0f, 0.7f)
                fadeOut.duration = FADE_DURATION
                val fadeIn = AlphaAnimation(0.7f, 0.0f)
                fadeIn.duration = FADE_DURATION
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    binding!!.screen.isVisible = true
                    binding!!.screen.alpha = 0.7f
                    binding!!.screen.startAnimation(fadeOut)
                } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    binding!!.screen.startAnimation(fadeIn)
                    binding!!.screen.alpha = 0f
                    binding!!.screen.isVisible = false
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

        })
    }

    override fun onResume() {
        super.onResume()
        if (binding == null) return
        lifecycleScope.launch {
            val tracksHeight = async { getHeightPickValueByViewBottom(binding!!.shareIcon) }
            val optionsHeight = async { getHeightPickValueByViewBottom(binding!!.titlePlt) }
            tracksSheetBehavior!!.peekHeight =
                tracksHeight.await() - requireContext().resources.getDimensionPixelSize(
                    R.dimen.playlist_icon_size
                )
            optionsSheetBehavior!!.peekHeight =
                optionsHeight.await() - requireContext().resources.getDimensionPixelSize(
                    R.dimen.playlist_options_view_top_margin
                )
        }
    }

    private val optionsSheetStateChangedCallback = object : BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) viewModel.showOptions(false)
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }

    private suspend fun getHeightPickValueByViewBottom(view: View): Int {
        var value = 0
        var leftToTry = BOOTTOMSHEET_PEEK_TRY_COUNT
        var isFirstTry = true
        if (binding != null) {
            while (value < 0 || leftToTry > 0) {
                if (!isFirstTry) delay(BOOTTOMSHEET_PEEK_RETRY_DELAY)
                with(binding!!) {
                    value =
                        (root.measuredHeight - view.y - view.measuredHeight).roundToInt()
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
                    .into(requireActivity().findViewById(R.id.playlist_image))
            }
            binding?.let { binding ->
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
            optionsSheetBinding?.playlistViewHolder?.let { holder ->
                if (viewModel.requirePlaylist() != null) {
                    PlaylistRowViewHolder(holder).bind(
                        item = viewModel.requirePlaylist()!!,
                        onClickListener = {})
                }
            }
            when (state.isOptionsBottomSheet) {
                true -> {//toShowOptions
                    tracksSheetBehavior?.isHideable = true
                    tracksSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
                    optionsSheetBehavior?.cancelBackProgress()
                    optionsSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
                }

                else -> {
                    optionsSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
                    tracksSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
                    tracksSheetBehavior?.isHideable = false
                }
            }
        }
    }

    override fun showMessage(message: PlaylistMessage) {
        val activity = requireActivity()
        if (activity is CanShowPlaylistMessage) {
            activity.showMessage(message)
        } else return
    }

    override fun showDialog(dialog: MaterialAlertDialogBuilder) {
        dialog.show()
    }
}