package com.example.playlistmaker.search.view

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.model.domain.Track
import com.example.playlistmaker.search.viewModel.SearchScreenState
import com.example.playlistmaker.search.viewModel.SearchViewModel
import java.util.Calendar.MILLISECOND
import java.util.Calendar.getInstance

class SearchActivity : AppCompatActivity() {
    companion object {
        const val SEARCH_PROMPT = "PROMPT"
        const val SEARCH_DEF = ""
        private const val CHOICE_DEBOUNCE_DELAY = 1100L
    }

    private lateinit var searchViewModel: SearchViewModel
    private var uiHandler: Handler? = null

    private var trackOnClickListener = object : TrackOnClickListener {
        override fun onClick(item: Track) {
            if (canMakeAChoice()) {
                searchViewModel.addTrackToHistory(item)
                searchViewModel.openTrack(item)
            }
        }

        private var choiceTimeStamp: Long = getInstance().get(MILLISECOND).toLong()
        private fun canMakeAChoice(): Boolean {
            val currentTime = getInstance().get(MILLISECOND).toLong()
            var result = true
            if ((currentTime - choiceTimeStamp) > CHOICE_DEBOUNCE_DELAY) {
                choiceTimeStamp = currentTime
                result = false
            }
            return result
        }
    }
    private var tracksAdapter: TrackAdapter? = null
    private var searchPromptString: String = ""
    private lateinit var binding: ActivitySearchBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uiHandler = Handler(Looper.getMainLooper())
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        searchViewModel = ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory()
        )[SearchViewModel::class.java]
        tracksAdapter = TrackAdapter(emptyList(), trackOnClickListener)
        binding.header.setNavigationOnClickListener { finish() }
        binding.clearSearchList.setOnClickListener { searchViewModel.doClearHistory() }
        binding.updateButton.setOnClickListener { searchViewModel.doRepeatSearch() }
        searchBarTextWatcherAttach()
        searchBarSetActionDone()//temporary use only
        clearTextAttach()
        startUpViewHolder()
        searchViewModel.getScreenState().observe(this) { render(it) }
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
    private fun render(screenState: SearchScreenState) {
        when (screenState) {
            is SearchScreenState.ResultHaveData -> {
                binding.progressBar.isVisible = false
                binding.beenSearchedTitle.isVisible = false
                binding.clearSearchList.isVisible = false
                binding.placeholderFrame.isVisible = false
                if (tracksAdapter?.tracks != screenState.tracks) {
                    tracksAdapter?.tracks = screenState.tracks
                    tracksAdapter?.notifyDataSetChanged()
                }
                binding.tracksRecyclerView.visibility = View.VISIBLE
            }

            SearchScreenState.ResultIsEmpty -> {
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

            SearchScreenState.Error -> {
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

            is SearchScreenState.HistoryHaveData -> {
                binding.progressBar.isVisible = false
                binding.placeholderFrame.isVisible = false
                binding.updateButton.isVisible = false
                if (tracksAdapter?.tracks != screenState.tracks) {
                    tracksAdapter?.tracks = screenState.tracks
                    tracksAdapter?.notifyDataSetChanged()
                }
                binding.beenSearchedTitle.visibility = View.VISIBLE
                binding.clearSearchList.visibility = View.VISIBLE
                binding.tracksRecyclerView.visibility = View.VISIBLE
            }

            SearchScreenState.HistoryIsEmpty -> {
                binding.progressBar.isVisible = false
                binding.placeholderFrame.isVisible = false
                binding.updateButton.isVisible = false
                binding.beenSearchedTitle.isVisible = false
                binding.clearSearchList.isVisible = false
                binding.tracksRecyclerView.isVisible = false
            }

            SearchScreenState.Loading -> {
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
                searchViewModel.doSearchTracks(binding.searchBar.text.toString())
            }
            false
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
                    searchViewModel.doSearchTracks("")
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty())
                    searchViewModel.doAutoSearchTracks(s.toString())
                else searchViewModel.doSearchTracks("")
            }
        }
        binding.searchBar.addTextChangedListener(searchBarTextWatcher)
    }

    private fun startUpViewHolder() {
        binding.tracksRecyclerView.adapter = tracksAdapter
        binding.tracksRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
}