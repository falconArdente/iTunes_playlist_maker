package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

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

    private fun mediaButtonAttach() {// так как нужно опробвать способы привязки
        val mediaButton = findViewById<Button>(R.id.btn_media)
        val mediaBtnClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                startActivity(Intent(this@MainActivity, MediaActivity::class.java))
            }
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