package com.example.playlistmaker.player.view

import android.app.Activity
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.View.OnClickListener
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlayerBinding
import com.example.playlistmaker.media.model.domain.Playlist
import com.example.playlistmaker.media.view.MESSAGE_DURATION
import com.example.playlistmaker.media.view.MESSAGE_TEXT
import com.example.playlistmaker.media.view.ui.CanShowPlaylistMessage
import com.example.playlistmaker.media.view.ui.PlaylistMessage
import com.example.playlistmaker.player.model.domain.GetTrackToPlayUseCase
import com.example.playlistmaker.player.model.domain.PlayState.NotReady
import com.example.playlistmaker.player.model.domain.PlayState.Paused
import com.example.playlistmaker.player.model.domain.PlayState.Playing
import com.example.playlistmaker.player.model.domain.PlayState.ReadyToPlay
import com.example.playlistmaker.player.view.ui.PlaylistRowRecyclerViewAdapter
import com.example.playlistmaker.player.viewModel.PlayerScreenState
import com.example.playlistmaker.player.viewModel.PlayerViewModel
import com.example.playlistmaker.root.FRAGMENT_LOAD_COMMAND
import com.example.playlistmaker.root.RootActivity
import com.example.playlistmaker.search.model.domain.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.Locale


class PlayerActivity : AppCompatActivity(), CanShowPlaylistMessage {
    private val viewModel by viewModel<PlayerViewModel>()
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private lateinit var binding: PlayerBinding
    private var playlistsAdapter: PlaylistRowRecyclerViewAdapter? = null
    private var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null

    private val playButtonOnClickListener = OnClickListener {
        binding.playButton.isEnabled = false
        val curPlayState = viewModel.getPlayerScreenState().value?.playState
        if (curPlayState == ReadyToPlay || curPlayState == Paused) viewModel.play()
        if (curPlayState == Playing) viewModel.pause()
    }

    private val favoriteButtonOnClickListener: OnClickListener = OnClickListener {
        binding.favoriteButton.isEnabled = false
        if (viewModel.getIsFavorite().value == true) viewModel.removeFromFavorites()
        else viewModel.addToFavorites()
    }
    private val getPlaylistCreationResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode != Activity.RESULT_OK) return@registerForActivityResult
            with(activityResult.data!!) {
                showMessage(
                    PlaylistMessage.HaveData(
                        message = this.getStringExtra(MESSAGE_TEXT) ?: "",
                        showTimeMillis = this.getLongExtra(MESSAGE_DURATION, 0L)
                    )
                )
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = PlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val provider by inject<GetTrackToPlayUseCase> { parametersOf(this) }
        viewModel.setTrackProvider(provider)

        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet))
        binding.header.setNavigationOnClickListener { finish() }
        binding.playButton.setOnClickListener(playButtonOnClickListener)
        binding.favoriteButton.setOnClickListener(favoriteButtonOnClickListener)
        binding.plusButton.setOnClickListener {
            viewModel.addToPlaylistButtonAction(
                bottomSheetBehavior!!
            )
        }
        findViewById<TextView>(R.id.new_playlist_button_add_playlist_bs).setOnClickListener {
            val intent = Intent(this, RootActivity::class.java)
            intent.putExtra(FRAGMENT_LOAD_COMMAND, R.id.createPlaylistFragment)
            getPlaylistCreationResult.launch(intent)
        }
        viewModel.getPlayerScreenState().observe(this) { renderPlayerPart(it) }
        viewModel.getIsFavorite().observe(this) { reDrawFavoriteButton(it) }
        viewModel.playlistsToObserve.observe(this) { renderPlaylistsBottomSheet(it) }
        viewModel.messageToObserve.observe(this) { showMessage(it) }
        playlistsAdapter =
            PlaylistRowRecyclerViewAdapter(emptyList(), viewModel.getPlaylistViewOnClickListener())
        findViewById<RecyclerView>(R.id.recycler_view_add_playlist_bs).adapter = playlistsAdapter
        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun reDrawFavoriteButton(isFavorite: Boolean) {
        if (isFavorite) binding.favoriteButton.setImageResource(R.drawable.favorite_on_button)
        else binding.favoriteButton.setImageResource(R.drawable.favorite_off_button)
        binding.favoriteButton.isEnabled = true
    }

    private fun renderPlayerPart(screenState: PlayerScreenState) {
        if (binding.trackTitle.text != screenState.track.trackTitle) {
            reDrawFavoriteButton(viewModel.getIsFavorite().value ?: false)
            placeTrackDataToElements(screenState.track)
        }

        when (screenState.playState) {
            ReadyToPlay -> {
                binding.playButton.background = AppCompatResources.getDrawable(
                    this@PlayerActivity, R.drawable.play_button
                )
                binding.currentPlayPosition.text = dateFormat.format(0L)
                binding.playButton.isEnabled = true
            }

            Paused -> {
                binding.playButton.background = AppCompatResources.getDrawable(
                    this@PlayerActivity, R.drawable.play_button
                )
                binding.playButton.isEnabled = true
            }

            Playing -> {
                binding.currentPlayPosition.text =
                    dateFormat.format(screenState.currentPosition.toLong())
                binding.playButton.background =
                    AppCompatResources.getDrawable(this@PlayerActivity, R.drawable.pause_button)
                if (!binding.playButton.isEnabled) binding.playButton.isEnabled = true
            }

            NotReady -> {
                binding.playButton.isEnabled = false
                binding.playButton.background =
                    AppCompatResources.getDrawable(this@PlayerActivity, R.drawable.pause_button)
            }
        }
    }

    private fun placeTrackDataToElements(track: Track) {
        binding.trackTitle.text = track.trackTitle
        binding.artistName.text = track.artistName
        binding.durationValue.text = dateFormat.format(track.duration.toLong())
        binding.albumValue.text = track.collectionName
        binding.yearValue.text = if (track.releaseDate.length > 3) track.releaseDate.substring(
            0, 4
        ) else track.releaseDate
        binding.genreValue.text = track.genre
        binding.countryValue.text = track.country
        val pattern = getString(R.string.itunes_small_image_postfix)
        val bigImagePath: String =
            if (track.artwork.lastIndexOf(pattern) > 0) track.artwork.replace(
                pattern, getString(R.string.itunes_big_image_postfix)
            )
            else track.artwork
        Glide.with(binding.trackImage).load(bigImagePath)
            .placeholder(R.drawable.placeholder_search_bar).fitCenter().centerCrop()
            .transform(RoundedCorners(binding.trackImage.resources.getDimensionPixelSize(R.dimen.track_row_image_corners)))
            .into(binding.trackImage)
    }

    private fun renderPlaylistsBottomSheet(playlists: List<Playlist>) {
        playlistsAdapter?.playlists = playlists
    }

    override fun showMessage(message: PlaylistMessage) {
        if (message is PlaylistMessage.Empty) return
        val messenger = findViewById<LinearLayout>(R.id.playlist_messenger)
        val textView = findViewById<TextView>(R.id.playlist_message_text_view)
        with((message as PlaylistMessage.HaveData)) {
            lifecycleScope.launch {
                textView.text = this@with.message
                messenger.isVisible = true
                delay(this@with.showTimeMillis)
                messenger.isVisible = false
            }
        }
    }
}