package com.example.playlistmaker

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.PlayerBinding
import com.google.gson.Gson
import java.util.Locale

private const val TRACK_KEY = "track"

class PlayerActivity : AppCompatActivity() {
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private lateinit var track: Track
    private lateinit var binding: PlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.header.setNavigationOnClickListener { finish() }
        track = getTrackFromIntent()
        placeTrackDataToElements(track)
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
}