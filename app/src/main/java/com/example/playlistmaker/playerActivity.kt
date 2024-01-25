package com.example.playlistmaker

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.util.Locale

private const val TRACK_KEY = "track"

class PlayerActivity : AppCompatActivity() {
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private lateinit var track: Track
    private lateinit var backButton: androidx.appcompat.widget.Toolbar
    private lateinit var trackImage: androidx.appcompat.widget.AppCompatImageView
    private lateinit var trackTitle: androidx.appcompat.widget.AppCompatTextView
    private lateinit var artistName: androidx.appcompat.widget.AppCompatTextView
    private lateinit var playButton: androidx.appcompat.widget.AppCompatImageButton
    private lateinit var currentPosition: androidx.appcompat.widget.AppCompatTextView
    private lateinit var plusButton: androidx.appcompat.widget.AppCompatImageButton
    private lateinit var favoriteButton: androidx.appcompat.widget.AppCompatImageButton
    private lateinit var duration: androidx.appcompat.widget.AppCompatTextView
    private lateinit var album: androidx.appcompat.widget.AppCompatTextView
    private lateinit var year: androidx.appcompat.widget.AppCompatTextView
    private lateinit var genre: androidx.appcompat.widget.AppCompatTextView
    private lateinit var country: androidx.appcompat.widget.AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player)
        boundVariablesToElements()
        backButton.setNavigationOnClickListener { finish() }
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
        trackTitle.text = track.trackName
        artistName.text = track.artistName
        duration.text = dateFormat.format(track.duration.toLong())
        album.text = track.collectionName
        year.text = if (track.releaseDate.length > 3) track.releaseDate.substring(
            0,
            4
        ) else track.releaseDate
        genre.text = track.primaryGenreName
        country.text = track.country
        val pattern = getString(R.string.itunes_small_image_postfix)
        val bigImagePath: String = if (track.artworkUrl100.lastIndexOf(pattern) > 0)
            track.artworkUrl100.replace(
                pattern,
                getString(R.string.itunes_big_image_postfix)
            )
        else track.artworkUrl100
        Glide.with(trackImage)
            .load(bigImagePath)
            .placeholder(R.drawable.placeholder_search_bar)
            .fitCenter()
            .centerCrop()
            .transform(RoundedCorners(trackImage.resources.getDimensionPixelSize(R.dimen.track_row_image_corners)))
            .into(trackImage)
    }

    private fun boundVariablesToElements() {
        trackImage = findViewById(R.id.track_image)
        backButton = findViewById(R.id.header)
        trackTitle = findViewById(R.id.track_title)
        artistName = findViewById(R.id.artist_name)
        playButton = findViewById(R.id.play_button)
        currentPosition = findViewById(R.id.current_play_position)
        plusButton = findViewById(R.id.plus_button)
        favoriteButton = findViewById(R.id.favorite_button)
        duration = findViewById(R.id.duration_value)
        album = findViewById(R.id.album_value)
        year = findViewById(R.id.year_value)
        genre = findViewById(R.id.genre_value)
        country = findViewById(R.id.country_value)
    }
}