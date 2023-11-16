package com.example.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        backButtonClickAttach()
        searchBarTextWatcherAttach()
        clearTextAttach()
    }

    private fun clearTextAttach() {
        val searchBar = findViewById<EditText>(R.id.search_bar)
        val xMark = findViewById<ImageView>(R.id.clear_icon)
        xMark.setOnClickListener { searchBar.setText("") }
    }

    private fun searchBarTextWatcherAttach() {
        val searchBar = findViewById<EditText>(R.id.search_bar)
        val xMark = findViewById<ImageView>(R.id.clear_icon)
        val searchBarTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    xMark.visibility = View.GONE
                } else {
                    xMark.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        searchBar.addTextChangedListener(searchBarTextWatcher)
    }

    override fun onResume() {
        super.onResume()
        val searchBar = findViewById<EditText>(R.id.search_bar)
        val xMark = findViewById<ImageView>(R.id.clear_icon)
        if (searchBar.text.isNullOrEmpty()) xMark.visibility = View.GONE
    }

    private fun backButtonClickAttach() {
        val backButton = findViewById<Button>(R.id.back_button)
        backButton.setOnClickListener { finish() }
    }
}