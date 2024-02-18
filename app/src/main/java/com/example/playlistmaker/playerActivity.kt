package com.example.playlistmaker

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.PlayerBinding
import com.google.gson.Gson
import java.util.Locale

private const val TRACK_KEY = "track"
private const val DURATION_RENEWAL_DELAY: Long = 420L

class PlayerActivity : AppCompatActivity() {
    companion object {
        enum class PlayerState {
            Default,
            Prepared,
            Playing,
            Paused,
        }
    }

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private lateinit var track: Track
    private lateinit var binding: PlayerBinding
    private var playerState = PlayerState.Default
    private val mediaPlayer = MediaPlayer()
    private var uiHandler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.header.setNavigationOnClickListener { finish() }
        track = getTrackFromIntent()
        placeTrackDataToElements(track)
        binding.playButton.isEnabled=false
        uiHandler = Handler(Looper.getMainLooper())
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.setOnPreparedListener {
            binding.playButton.background =
                AppCompatResources.getDrawable(this, R.drawable.play_button)
            playerState = PlayerState.Prepared
            binding.playButton.isEnabled=true
        }
        mediaPlayer.prepareAsync()
        binding.playButton.setOnClickListener { pushPlayButton() }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.stop()
    }

    private fun getTrackFromIntent(): Track {
        val json = Gson()
        val tempString = intent.getStringExtra(TRACK_KEY)
        if (tempString.isNullOrEmpty()) finish()
        return json.fromJson(tempString, Track::class.java)
    }

    private fun placeTrackDataToElements(track: Track) {
        binding.trackTitle.text = track.trackName
        binding.artistName.text = track.artistName
        binding.durationValue.text = dateFormat.format(track.duration.toLong())
        binding.albumValue.text = track.collectionName
        binding.yearValue.text = if (track.releaseDate.length > 3) track.releaseDate.substring(
            0,
            4
        ) else track.releaseDate
        binding.genreValue.text = track.primaryGenreName
        binding.countryValue.text = track.country
        val pattern = getString(R.string.itunes_small_image_postfix)
        val bigImagePath: String = if (track.artworkUrl100.lastIndexOf(pattern) > 0)
            track.artworkUrl100.replace(
                pattern,
                getString(R.string.itunes_big_image_postfix)
            )
        else track.artworkUrl100
        Glide.with(binding.trackImage)
            .load(bigImagePath)
            .placeholder(R.drawable.placeholder_search_bar)
            .fitCenter()
            .centerCrop()
            .transform(RoundedCorners(binding.trackImage.resources.getDimensionPixelSize(R.dimen.track_row_image_corners)))
            .into(binding.trackImage)
    }

    private fun pushPlayButton() {
        when (playerState) {
            PlayerState.Prepared, PlayerState.Paused -> {
                mediaPlayer.start()
                playerState = PlayerState.Playing
                binding.playButton.background =
                    AppCompatResources.getDrawable(this, R.drawable.pause_button)
                startDurationUpdate()
            }

            PlayerState.Playing -> {
                mediaPlayer.pause()
                playerState = PlayerState.Paused
                binding.playButton.background =
                    AppCompatResources.getDrawable(this, R.drawable.play_button)
                uiHandler?.removeCallbacks { startDurationUpdate() }
            }

            else -> Toast.makeText(this, getString(R.string.sound_stream_error), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun startDurationUpdate() {
        uiHandler?.postDelayed({
            binding.currentPlayPosition.text =
                dateFormat.format(mediaPlayer.currentPosition.toLong())
            startDurationUpdate()
        }, DURATION_RENEWAL_DELAY)
    }
}