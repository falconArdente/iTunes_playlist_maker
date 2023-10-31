package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val btnBack=findViewById<Button>(R.id.back_button)
        val goMain=Intent(this,MainActivity::class.java)
        btnBack.setOnClickListener {startActivity(goMain)}
    }
}