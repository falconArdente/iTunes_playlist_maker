package com.example.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var localPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        searchButtonAttach()
        mediaButtonAttach()
        settingsButtonAttach()
    }

    private fun localPreferencesUpdate(){
       // localPreferences=getSharedPreferences()
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