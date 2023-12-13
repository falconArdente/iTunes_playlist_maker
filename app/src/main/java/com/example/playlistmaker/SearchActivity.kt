package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    companion object {
        const val SEARCH_PROMPT = "PROMPT"
        const val SEARCH_DEF = ""
    }

    private val url: String = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesApi::class.java)
    private val tracks: ArrayList<Track> = arrayListOf()
    private val tracksAdapter = TrackAdapter(tracks)
    private var searchPromptString: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        backButtonClickAttach()
        searchBarTextWatcherAttach()
        searchBarSetActionDone()//temporary use only
        clearTextAttach()
        updateButtonClickAttach()
    }

    private fun updateButtonClickAttach() {
        findViewById<TextView>(R.id.update_button).setOnClickListener { sendRequest() }
    }

    private fun searchBarSetActionDone() {
        val searchBar = findViewById<EditText>(R.id.search_bar)
        searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendRequest()
            }
            false
        }
    }

    private fun sendRequest() {
        val searchBar = findViewById<EditText>(R.id.search_bar)
        if (searchBar.text.isNotEmpty()) {
            iTunesService.findTrack(searchBar.text.toString()).enqueue(object :
                Callback<TrackSearchResponse> {
                override fun onResponse(
                    call: Call<TrackSearchResponse>,
                    response: Response<TrackSearchResponse>
                ) {
                    if (response.code() == 200) {
                        println("data received at 200")
                        tracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.addAll(response.body()?.results!!)
                            tracksAdapter.notifyDataSetChanged()
                        }
                        if (tracks.isEmpty()) {
                            showTracks(true)

                        } else {
                            showTracks()
                        }
                    } else {
                        showErrorSign()
                    }
                }

                override fun onFailure(call: Call<TrackSearchResponse>, t: Throwable) {
                    showErrorSign()
                }

            })
        }
    }

    private fun showErrorSign() {
        val placeholderFrame =
            findViewById<LinearLayout>(R.id.placeholder_frame)
        val trackListRecyclerView =
            findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.tracks_recycler_view)
        val updateButton = findViewById<TextView>(R.id.update_button)
        trackListRecyclerView.visibility = View.GONE
        val text = findViewById<TextView>(R.id.status_text)
        val statusImageView = findViewById<ImageView>(R.id.status_image)
        text.text = applicationContext.getString(R.string.search_status_connection_problem)
        updateButton.visibility = View.VISIBLE
        statusImageView.setImageDrawable(applicationContext.getDrawable(R.drawable.no_wifi))
        placeholderFrame.visibility = View.VISIBLE
    }

    private fun showTracks(tracksIsNone: Boolean = false) {
        val placeholderFrame =
            findViewById<LinearLayout>(R.id.placeholder_frame)
        val trackListRecyclerView =
            findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.tracks_recycler_view)
        val updateButton = findViewById<TextView>(R.id.update_button)
        val statusImageView = findViewById<ImageView>(R.id.status_image)
        if (tracksIsNone) {
            trackListRecyclerView.visibility = View.GONE
            val text = findViewById<TextView>(R.id.status_text)
            text.text = applicationContext.getString(R.string.search_status_nothing)
            updateButton.visibility = View.GONE
            statusImageView.setImageDrawable(applicationContext.getDrawable(R.drawable.sad_smile))
            placeholderFrame.visibility = View.VISIBLE
        } else {
            trackListRecyclerView.visibility = View.VISIBLE
            placeholderFrame.visibility = View.GONE
        }
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
        val trackListRecyclerView =
            findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.tracks_recycler_view)
        val searchBarTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    xMark.visibility = View.GONE
                    trackListRecyclerView.visibility=View.GONE
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
        val tracksRecyclerView =
            findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.tracks_recycler_view)
        tracksRecyclerView.adapter = tracksAdapter
    }
}