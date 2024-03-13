package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.HistoryInteractor
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.SearchInteractor.TracksConsumer
import com.example.playlistmaker.search.domain.Track
import com.google.gson.Gson
import java.util.Calendar.*

class SearchActivity : AppCompatActivity() {
    companion object {
        const val SEARCH_PROMPT = "PROMPT"
        const val SEARCH_DEF = ""
        const val TRACK_KEY = "track"
        private const val AUTO_SEND_REQUEST_DELAY = 2000L
        private const val CHOICE_DEBOUNCE_DELAY = 1100L

        enum class State {
            History,
            CleanHistory,
            SearchGot,
            SearchIsEmpty,
            Error,
            WaitingForResponse,
        }
    }

    private var history: HistoryInteractor? = null
    private var search: SearchInteractor? = null
    private var searchTracks = arrayListOf<Track>()
    private var uiHandler: Handler? = null
    private var choiceTimeStamp: Long = 0L
    private var trackOnClickListener = object : TrackOnClickListener {
        override fun onClick(item: Track) {
            if (canMakeAChoice()) {
                history?.addTrackToHistory(item)
                val intent =
                    Intent(this@SearchActivity, PlayerActivity::class.java)
                val json = Gson()
                intent.putExtra(TRACK_KEY, json.toJson(item))
                ContextCompat.startActivity(this@SearchActivity, intent, null)
            }
        }
    }
    private var tracksAdapter: TrackAdapter? = null
    private var searchPromptString: String = ""
    private lateinit var binding: ActivitySearchBinding
    private fun canMakeAChoice(): Boolean {
        val currentTime = getInstance().get(MILLISECOND).toLong()
        return if ((currentTime - choiceTimeStamp) < CHOICE_DEBOUNCE_DELAY) {
            choiceTimeStamp = currentTime
            true
        } else false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uiHandler = Handler(Looper.getMainLooper())
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        history = Creator.provideHistoryInteractor(this)
        tracksAdapter =
            TrackAdapter(history?.getTracksHistory() ?: emptyList(), trackOnClickListener)
        search = Creator.provideSearchInteractor()
        backButtonClickAttach()
        searchBarTextWatcherAttach()
        searchBarSetActionDone()//temporary use only
        clearTextAttach()
        startUpViewHolder()
        binding.updateButton.setOnClickListener { uiHandler?.post(doSearchTracksOfEditText) }
        clearHistoryButtonClickAttach()
    }

    val searchConsumer = object : TracksConsumer {
        override fun consume(foundTracks: List<Track>) {
            uiHandler?.post {
                searchTracks.clear()
                searchTracks.addAll(foundTracks)
                if (searchTracks.isEmpty()) {
                    showLayout(State.SearchIsEmpty)
                } else {
                    showLayout(State.SearchGot)
                }
            }
        }
    }

    val doSearchTracksOfEditText = object : Runnable {
        override fun run() {
            uiHandler?.removeCallbacks(this)
            val prompt = binding.searchBar.text.toString()
            if (prompt.isNotEmpty()) search?.searchTracks(
                expression = prompt,
                trackConsumer = searchConsumer,
                errorConsumer = searchErrorConsumer
            )
        }
    }

    val searchErrorConsumer = object : SearchInteractor.ErrorConsumer {
        override fun consume() {
            uiHandler?.post { showLayout(State.Error) }
        }

    }

    override fun onResume() {
        super.onResume()
        if (binding.searchBar.text.isNullOrEmpty()) {
            binding.clearIcon.visibility = View.GONE
            val historyTracks = history?.getTracksHistory() ?: emptyList()
            if (historyTracks.isEmpty()) showLayout(State.CleanHistory)
            else showLayout(State.History)
        }
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
        binding.searchBar.setText(
            savedInstanceState?.getString(SEARCH_PROMPT)
                ?: SEARCH_DEF
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun showLayout(state: State) {
        when (state) {
            State.SearchGot -> {
                binding.progressBar.isVisible = false
                binding.beenSearchedTitle.isVisible = false
                binding.clearSearchList.isVisible = false
                binding.placeholderFrame.isVisible = false
                if (tracksAdapter?.tracks != searchTracks) {
                    tracksAdapter?.tracks = searchTracks
                    tracksAdapter?.notifyDataSetChanged()
                }
                binding.tracksRecyclerView.visibility = View.VISIBLE
            }

            State.SearchIsEmpty -> {
                binding.progressBar.isVisible = false
                binding.beenSearchedTitle.isVisible = false
                binding.clearSearchList.isVisible = false
                binding.updateButton.isVisible = false
                binding.tracksRecyclerView.isVisible = false
                binding.statusText.text = this.getString(R.string.search_status_nothing)
                binding.statusImage.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.image_sad_smile_mus
                    )
                )
                binding.placeholderFrame.visibility = View.VISIBLE
            }

            State.Error -> {
                binding.progressBar.isVisible = false
                binding.beenSearchedTitle.isVisible = false
                binding.clearSearchList.isVisible = false
                binding.tracksRecyclerView.isVisible = false
                binding.statusText.text =
                    applicationContext.getString(R.string.search_status_connection_problem)
                binding.statusImage.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.image_no_wifi_mus
                    )
                )
                binding.updateButton.visibility = View.VISIBLE
                binding.placeholderFrame.visibility = View.VISIBLE
            }

            State.History -> {
                binding.progressBar.isVisible = false
                binding.placeholderFrame.isVisible = false
                binding.updateButton.isVisible = false
                val historyTracks = history?.getTracksHistory() ?: emptyList()

                if (tracksAdapter?.tracks != historyTracks) {
                    tracksAdapter?.tracks = historyTracks
                    tracksAdapter?.notifyDataSetChanged()
                }
                binding.beenSearchedTitle.visibility = View.VISIBLE
                binding.clearSearchList.visibility = View.VISIBLE
                binding.tracksRecyclerView.visibility = View.VISIBLE
            }

            State.CleanHistory -> {
                binding.progressBar.isVisible = false
                binding.placeholderFrame.isVisible = false
                binding.updateButton.isVisible = false
                binding.beenSearchedTitle.isVisible = false
                binding.clearSearchList.isVisible = false
                binding.tracksRecyclerView.isVisible = false
            }

            State.WaitingForResponse -> {
                binding.placeholderFrame.isVisible = false
                binding.updateButton.isVisible = false
                binding.beenSearchedTitle.isVisible = false
                binding.clearSearchList.isVisible = false
                binding.tracksRecyclerView.isVisible = false
                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun searchBarSetActionDone() {
        binding.searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                uiHandler?.post(doSearchTracksOfEditText)
            }
            false
        }
    }

    private fun clearHistoryButtonClickAttach() {
        binding.clearSearchList.setOnClickListener {
            history?.clearHistory()
            showLayout(State.CleanHistory)
        }
    }

    private fun clearTextAttach() {
        binding.clearIcon.setOnClickListener {
            binding.searchBar.setText("")
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
                    binding.clearIcon.visibility = View.GONE
                } else {
                    binding.clearIcon.visibility = View.VISIBLE
                    uiHandler?.postDelayed(doSearchTracksOfEditText, AUTO_SEND_REQUEST_DELAY)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                searchPromptString = binding.searchBar.text.toString()
                if (searchPromptString.isEmpty()) showLayout(State.History)
            }
        }
        binding.searchBar.addTextChangedListener(searchBarTextWatcher)
    }

    private fun backButtonClickAttach() {
        val header = findViewById<androidx.appcompat.widget.Toolbar>(R.id.header)
        header.setNavigationOnClickListener { finish() }
    }

    private fun startUpViewHolder() {
        binding.tracksRecyclerView.adapter = tracksAdapter
        binding.tracksRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
}