package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchActivity : AppCompatActivity() {
    companion object {
        const val SEARCH_PROMPT = "PROMPT"
        const val SEARCH_DEF = ""
        const val TRACK_KEY = "track"

        enum class State {
            History,
            CleanHistory,
            SearchGot,
            SearchIsEmpty,
            Error,
        }
    }

    private val iTunesService = Utility.initItunesService()
    private val searchTracks: ArrayList<Track> = arrayListOf()
    private lateinit var history: SearchHistory
    private var historyTracks: ArrayList<Track> = arrayListOf()
    private var trackOnClickListener = object : TrackOnClickListener {
        override fun onClick(item: Track) {
            (this@SearchActivity.applicationContext as App).history.addTrack(item)
            val intent =
                Intent(this@SearchActivity.applicationContext, PlayerActivity::class.java)
            val json = Gson()
            intent.putExtra(TRACK_KEY, json.toJson(item))
            ContextCompat.startActivity(this@SearchActivity.applicationContext, intent, null)
        }
    }
    private val tracksAdapter = TrackAdapter(historyTracks, trackOnClickListener)
    private var searchPromptString: String = ""
    private lateinit var placeholderFrame: LinearLayout
    private lateinit var trackListRecyclerView: RecyclerView
    private lateinit var searchBar: EditText
    private lateinit var xMark: ImageView
    private lateinit var updateButton: TextView
    private lateinit var statusImageView: ImageView
    private lateinit var statusText: TextView
    private lateinit var clearHistoryButton: androidx.appcompat.widget.AppCompatTextView
    private lateinit var beenSearchedTitle: androidx.appcompat.widget.AppCompatTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        history= (this.applicationContext as App).history
        historyTracks=history.tracks
        placeholderFrame = findViewById(R.id.placeholder_frame)
        trackListRecyclerView = findViewById(R.id.tracks_recycler_view)
        searchBar = findViewById(R.id.search_bar)
        xMark = findViewById(R.id.clear_icon)
        updateButton = findViewById(R.id.update_button)
        statusImageView = findViewById(R.id.status_image)
        statusText = findViewById(R.id.status_text)
        updateButton = findViewById(R.id.update_button)
        beenSearchedTitle = findViewById(R.id.been_searched_title)
        backButtonClickAttach()
        searchBarTextWatcherAttach()
        searchBarSetActionDone()//temporary use only
        clearTextAttach()
        startUpViewHolder()
        findViewById<TextView>(R.id.update_button).setOnClickListener { sendRequest() }
        clearHistoryButtonClickAttach()
    }

    override fun onResume() {
        super.onResume()
        if (searchBar.text.isNullOrEmpty()) xMark.visibility = View.GONE
        history.getFromVault()
        if (history.tracks.isEmpty()) showLayout(State.CleanHistory)
        else showLayout(State.History)
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
        searchBar.setText(
            savedInstanceState?.getString(SEARCH_PROMPT)
                ?: SEARCH_DEF
        )
    }

    fun showLayout(state: State) {
        when (state) {
            State.SearchGot -> {
                beenSearchedTitle.isVisible = false
                clearHistoryButton.isVisible = false
                placeholderFrame.isVisible = false
                if (tracksAdapter.tracks != searchTracks) {
                    tracksAdapter.tracks = searchTracks
                    tracksAdapter.notifyDataSetChanged()
                }
                trackListRecyclerView.visibility = View.VISIBLE
            }

            State.SearchIsEmpty -> {
                beenSearchedTitle.isVisible = false
                clearHistoryButton.isVisible = false
                updateButton.isVisible = false
                trackListRecyclerView.isVisible = false
                statusText.text = this.getString(R.string.search_status_nothing)
                statusImageView.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.image_sad_smile_mus
                    )
                )
                placeholderFrame.visibility = View.VISIBLE
            }

            State.Error -> {
                beenSearchedTitle.isVisible = false
                clearHistoryButton.isVisible = false
                trackListRecyclerView.isVisible = false
                statusText.text =
                    applicationContext.getString(R.string.search_status_connection_problem)
                statusImageView.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.image_no_wifi_mus
                    )
                )
                updateButton.visibility = View.VISIBLE
                placeholderFrame.visibility = View.VISIBLE
            }

            State.History -> {
                placeholderFrame.isVisible = false
                updateButton.isVisible = false
                if (tracksAdapter.tracks != historyTracks) {
                    tracksAdapter.tracks = historyTracks
                    tracksAdapter.notifyDataSetChanged()
                }
                beenSearchedTitle.visibility = View.VISIBLE
                clearHistoryButton.visibility = View.VISIBLE
                trackListRecyclerView.visibility = View.VISIBLE
            }

            State.CleanHistory -> {
                placeholderFrame.isVisible = false
                updateButton.isVisible = false
                beenSearchedTitle.isVisible = false
                clearHistoryButton.isVisible = false
                trackListRecyclerView.isVisible = false
            }
        }
    }

    private fun sendRequest() {
        if (searchBar.text.isNotEmpty()) {
            iTunesService.findTrack(searchBar.text.toString()).enqueue(object :
                Callback<TrackSearchResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<TrackSearchResponse>,
                    response: Response<TrackSearchResponse>
                ) {
                    if (response.code() == 200) {
                        searchTracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            searchTracks.addAll(response.body()?.results!!)
                            tracksAdapter.notifyDataSetChanged()
                        }
                        if (searchTracks.isEmpty()) {
                            showLayout(State.SearchIsEmpty)
                        } else {
                            showLayout(State.SearchGot)
                        }
                    } else {
                        showLayout(State.Error)
                        Log.e("servRequests", "$response.code()")
                    }
                }

                override fun onFailure(call: Call<TrackSearchResponse>, t: Throwable) {
                    showLayout(State.Error)
                }
            })
        }
    }

    private fun searchBarSetActionDone() {
        searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendRequest()
            }
            false
        }
    }

    private fun clearHistoryButtonClickAttach() {
        clearHistoryButton =
            findViewById(R.id.clear_search_list)
        clearHistoryButton.setOnClickListener {
            history.clear()
            showLayout(State.CleanHistory)
        }
    }

    private fun clearTextAttach() {
        xMark.setOnClickListener {
            searchBar.setText("")
            this.currentFocus?.let { view ->
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    private fun searchBarTextWatcherAttach() {
        val searchBarTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    xMark.visibility = View.GONE
                } else {
                    xMark.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                searchPromptString = searchBar.text.toString()
                if (searchPromptString.isEmpty()) showLayout(State.History)
            }
        }
        searchBar.addTextChangedListener(searchBarTextWatcher)
    }

    private fun backButtonClickAttach() {
        val header = findViewById<androidx.appcompat.widget.Toolbar>(R.id.header)
        header.setNavigationOnClickListener { finish() }
    }

    private fun startUpViewHolder() {
        trackListRecyclerView.adapter = tracksAdapter
        trackListRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
}