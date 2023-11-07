package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        backButtonAttach()
    }

    private fun backButtonAttach() {
        val backButton = findViewById<Button>(R.id.back_button)
        val goMain = Intent(this, MainActivity::class.java)
        backButton.setOnClickListener { startActivity(goMain) }
    }
}