package com.example.playlistmaker.presentation

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.PlayerBinding
import com.example.playlistmaker.domain.api.MusicPlayInteractor
import com.example.playlistmaker.domain.api.PlayState
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import java.util.Locale

private const val TRACK_KEY = "track"
private const val DURATION_RENEWAL_DELAY: Long = 421L

class PlayerActivity : AppCompatActivity() {

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private var track: Track = Track("", "", "", "", "", "", "", "", "", "")
    private lateinit var binding: PlayerBinding
    private var playerInteractor: MusicPlayInteractor? = null
    private var uiHandler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.header.setNavigationOnClickListener { finish() }
        track = getTrackFromIntent()
        placeTrackDataToElements(track)
        uiHandler = Handler(Looper.getMainLooper())
        prepareMediaPlayerStuff()
        binding.playButton.setOnClickListener { pushPlayButton() }
    }

    private fun pushPlayButton() {
        when (playerInteractor?.getCurrentState()) {
            PlayState.ReadyToPlay, PlayState.Paused -> {
                playerInteractor?.play()
            }

            PlayState.Playing -> {
                playerInteractor?.pause()
            }

            else -> {
            }
        }
    }

    override fun onPause() {
        super.onPause()
        playerInteractor?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerInteractor?.stop()
    }

    private fun getTrackFromIntent(): Track {
        val json = Gson()
        val tempString = intent.getStringExtra(TRACK_KEY)
        if (tempString.isNullOrEmpty()) finish()
        return json.fromJson(tempString, Track::class.java)
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

    private val musicPlayEventsConsumer = object : MusicPlayInteractor.MusicPlayEventsConsumer {
        override fun playEventConsume() {
            binding.playButton.background =
                AppCompatResources.getDrawable(this@PlayerActivity, R.drawable.pause_button)
            uiHandler?.post(startDurationUpdate)
        }

        override fun pauseEventConsume() {
            uiHandler?.removeCallbacks(startDurationUpdate)
            binding.playButton.background =
                AppCompatResources.getDrawable(this@PlayerActivity, R.drawable.play_button)
        }

        override fun readyToPlayEventConsume() {
            uiHandler?.removeCallbacks(startDurationUpdate)
            binding.playButton.background =
                AppCompatResources.getDrawable(this@PlayerActivity, R.drawable.play_button)
            binding.playButton.isEnabled = true
            uiHandler?.post { binding.currentPlayPosition.text = dateFormat.format(0L) }
        }
    }

    private fun prepareMediaPlayerStuff() {
        binding.playButton.isEnabled = false
        playerInteractor = Creator.provideMusicPlayerInteractor(musicPlayEventsConsumer)
        playerInteractor?.setTrack(trackToPlay = track)
    }

    private val startDurationUpdate: Runnable = object : Runnable {
        override fun run() {
            binding.currentPlayPosition.text =
                dateFormat.format(playerInteractor?.getCurrentPosition())
            uiHandler?.postDelayed(this, DURATION_RENEWAL_DELAY)
        }
    }
}