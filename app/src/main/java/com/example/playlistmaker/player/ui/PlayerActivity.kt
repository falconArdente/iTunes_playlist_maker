package com.example.playlistmaker.player.ui

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.View.OnClickListener
import androidx.activity.ComponentActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.PlayerBinding
import com.example.playlistmaker.player.domain.PlayState.NotReady
import com.example.playlistmaker.player.domain.PlayState.Paused
import com.example.playlistmaker.player.domain.PlayState.Playing
import com.example.playlistmaker.player.domain.PlayState.ReadyToPlay
import com.example.playlistmaker.player.viewModel.PlayerScreenState
import com.example.playlistmaker.player.viewModel.PlayerViewModel
import com.example.playlistmaker.search.domain.Track
import java.util.Locale

class PlayerActivity : ComponentActivity() {
    private lateinit var viewModel: PlayerViewModel
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private lateinit var binding: PlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this,
            PlayerViewModel.getViewModelFactory()
        )[PlayerViewModel::class.java]
        viewModel.setTrackProvider(Creator.provideTrackToPlayUseCase(this))
        binding.header.setNavigationOnClickListener { finish() }
        binding.playButton.setOnClickListener(playButtonOnClickListener)
        viewModel.getPlayerScreenState().observe(this) { render(it) }
    }

    private val playButtonOnClickListener = OnClickListener {
        binding.playButton.isEnabled=false
        val curPlayState = viewModel.getPlayerScreenState().value?.playState
        if (curPlayState == ReadyToPlay || curPlayState == Paused) viewModel.play()
        if (curPlayState == Playing) viewModel.pause()
    }

    private fun render(screenState: PlayerScreenState) {
        if (binding.trackTitle.text != screenState.track.trackTitle) placeTrackDataToElements(
            screenState.track
        )

        when (screenState.playState) {
            ReadyToPlay -> {
                binding.playButton.background =
                    AppCompatResources.getDrawable(
                        this@PlayerActivity,
                        R.drawable.play_button
                    )
                binding.currentPlayPosition.text =
                    dateFormat.format(0L)
                binding.playButton.isEnabled=true
            }

            Paused -> {
                binding.playButton.background =
                    AppCompatResources.getDrawable(
                        this@PlayerActivity,
                        R.drawable.play_button
                    )
                binding.playButton.isEnabled=true
            }

            Playing -> {
                binding.currentPlayPosition.text =
                    dateFormat.format(screenState.currentPosition.toLong())
                binding.playButton.background =
                    AppCompatResources.getDrawable(this@PlayerActivity, R.drawable.pause_button)
                if (!binding.playButton.isEnabled)binding.playButton.isEnabled=true
            }

            NotReady ->{
                binding.playButton.isEnabled=false
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
            0,
            4
        ) else track.releaseDate
        binding.genreValue.text = track.genre
        binding.countryValue.text = track.country
        val pattern = getString(R.string.itunes_small_image_postfix)
        val bigImagePath: String = if (track.artwork.lastIndexOf(pattern) > 0)
            track.artwork.replace(
                pattern,
                getString(R.string.itunes_big_image_postfix)
            )
        else track.artwork
        Glide.with(binding.trackImage)
            .load(bigImagePath)
            .placeholder(R.drawable.placeholder_search_bar)
            .fitCenter()
            .centerCrop()
            .transform(RoundedCorners(binding.trackImage.resources.getDimensionPixelSize(R.dimen.track_row_image_corners)))
            .into(binding.trackImage)
    }
}