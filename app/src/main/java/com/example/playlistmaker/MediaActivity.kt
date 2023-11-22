package com.example.playlistmaker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MediaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)
        backButtonClickAttach()
    }

    private fun backButtonClickAttach() {
        val header = findViewById<androidx.appcompat.widget.Toolbar>(R.id.header)
        header.setNavigationOnClickListener {
            finish()
        }
    }
}