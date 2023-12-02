package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {
    companion object {
        const val SEARCH_PROMPT = "PROMPT"
        const val SEARCH_DEF = ""
    }

    private val tracks: ArrayList<Track> = arrayListOf()
    private var searchPromptString: String = ""
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
        xMark.setOnClickListener {
            searchBar.setText("")
            this.currentFocus?.let { view ->
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    private fun searchBarTextWatcherAttach() {
        val searchBar = findViewById<EditText>(R.id.search_bar)
        val xMark = findViewById<ImageView>(R.id.clear_icon)
        val searchBarTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    xMark.visibility = View.GONE
                } else {
                    xMark.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                searchPromptString = searchBar.text.toString()
            }
        }
        searchBar.addTextChangedListener(searchBarTextWatcher)
    }

    override fun onResume() {
        super.onResume()
        val searchBar = findViewById<EditText>(R.id.search_bar)
        val xMark = findViewById<ImageView>(R.id.clear_icon)
        if (searchBar.text.isNullOrEmpty()) xMark.visibility = View.GONE
        startUpViewHolder()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString(SEARCH_PROMPT, searchPromptString)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        val searchBar = findViewById<EditText>(R.id.search_bar)
        searchBar.setText(
            savedInstanceState?.getString(SEARCH_PROMPT)
                ?: SEARCH_DEF
        )
    }

    private fun backButtonClickAttach() {
        val header = findViewById<androidx.appcompat.widget.Toolbar>(R.id.header)
        header.setNavigationOnClickListener { finish() }
    }

    private fun startUpViewHolder() {
        Utility.toMokATrackList(tracks)
        val tracksAdapter = TrackAdapter(tracks)
        val tracksRecyclerView =
            findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.tracks_recycler_view)
        tracksRecyclerView.adapter = tracksAdapter
    }
}