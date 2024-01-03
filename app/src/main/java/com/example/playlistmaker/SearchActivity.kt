package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    companion object {
        const val SEARCH_PROMPT = "PROMPT"
        const val SEARCH_DEF = ""
    }

    private val iTunesService = Utility.initItunesService()
    private val tracks: ArrayList<Track> = arrayListOf()
    private val tracksAdapter = TrackAdapter(tracks)
    private val historyAdapter = TrackAdapter(App.history.tracks)


    private var searchPromptString: String = ""
    private lateinit var placeholderFrame: LinearLayout
    private lateinit var trackListRecyclerView: RecyclerView
    private lateinit var searchBar: EditText
    private lateinit var xMark: ImageView
    private lateinit var updateButton: TextView
    private lateinit var statusImageView: ImageView
    private lateinit var text: TextView
    private lateinit var clearHistoryButton: androidx.appcompat.widget.AppCompatTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        placeholderFrame = findViewById(R.id.placeholder_frame)
        trackListRecyclerView = findViewById(R.id.tracks_recycler_view)
        searchBar = findViewById(R.id.search_bar)
        xMark = findViewById(R.id.clear_icon)
        updateButton = findViewById(R.id.update_button)
        statusImageView = findViewById(R.id.status_image)
        text = findViewById(R.id.status_text)
        updateButton = findViewById(R.id.update_button)
        backButtonClickAttach()
        searchBarTextWatcherAttach()
        searchBarSetActionDone()//temporary use only
        clearTextAttach()
        startUpViewHolder()
        findViewById<TextView>(R.id.update_button).setOnClickListener { sendRequest() }
        clearHistoryButtonClickAttach()
    }

    override fun onPause() {
        super.onPause()
        App.history.saveToVault()
    }

    private fun searchBarSetActionDone() {
        searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendRequest()
            }
            false
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
                        Log.e("servRequests", "$response.code()")
                    }
                }

                override fun onFailure(call: Call<TrackSearchResponse>, t: Throwable) {
                    showErrorSign()
                }

            })
        }
    }

    private fun showErrorSign() {
        trackListRecyclerView.visibility = View.GONE
        text.text = applicationContext.getString(R.string.search_status_connection_problem)
        updateButton.visibility = View.VISIBLE
        statusImageView.setImageDrawable(
            AppCompatResources.getDrawable(
                this,
                R.drawable.image_no_wifi_mus
            )
        )
        placeholderFrame.visibility = View.VISIBLE
    }

    private fun showTracks(tracksIsNone: Boolean = false) {
        if (tracksIsNone) {
            trackListRecyclerView.visibility = View.GONE
            val text = findViewById<TextView>(R.id.status_text)
            text.text = this.getString(R.string.search_status_nothing)
            updateButton.visibility = View.GONE
            statusImageView.setImageDrawable(
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.image_sad_smile_mus
                )
            )
            placeholderFrame.visibility = View.VISIBLE
        } else {
            trackListRecyclerView.visibility = View.VISIBLE
            placeholderFrame.visibility = View.GONE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearHistoryButtonClickAttach() {
        clearHistoryButton =
            findViewById(R.id.clear_search_list)
        clearHistoryButton.setOnClickListener {
            App.history.clear()
            trackListRecyclerView.adapter?.notifyDataSetChanged()
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
                    placeholderFrame.visibility = View.GONE
                } else {
                    xMark.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                searchPromptString = searchBar.text.toString()
                if (searchPromptString.isEmpty()) {
                    trackListRecyclerView.swapAdapter(historyAdapter, false)
                } else {
                    trackListRecyclerView.swapAdapter(tracksAdapter, true)
                }
            }
        }
        searchBar.addTextChangedListener(searchBarTextWatcher)
    }

    override fun onResume() {
        super.onResume()
        if (searchBar.text.isNullOrEmpty()) xMark.visibility = View.GONE
        App.history.getFromVault()
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

    private fun backButtonClickAttach() {
        val header = findViewById<androidx.appcompat.widget.Toolbar>(R.id.header)
        header.setNavigationOnClickListener { finish() }
    }

    private fun startUpViewHolder() {
        trackListRecyclerView.adapter = TrackAdapter(App.history.tracks)
        trackListRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
}