package com.example.playlistmaker.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.media.ui.MediaActivity
import com.example.playlistmaker.search.ui.SearchActivity
import com.example.playlistmaker.settings.ui.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        searchButtonAttach()
        mediaButtonAttach()
        settingsButtonAttach()
    }

    private fun searchButtonAttach() {
        val searchButton = findViewById<Button>(R.id.btn_search)
        searchButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, SearchActivity::class.java))
        }
    }

    private fun mediaButtonAttach() {
        val mediaButton = findViewById<Button>(R.id.btn_media)
        val mediaBtnClickListener: View.OnClickListener = View.OnClickListener {
            startActivity(Intent(this@MainActivity, MediaActivity::class.java))
        }
        mediaButton.setOnClickListener(mediaBtnClickListener)
    }

    private fun settingsButtonAttach() {
        mediaButtonAttach()
        val settingsButton = findViewById<Button>(R.id.btn_settings)
        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}