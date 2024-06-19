package com.example.playlistmaker.search.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.search.model.domain.Track
import com.example.playlistmaker.search.view.ui.TrackAdapter
import com.example.playlistmaker.search.view.ui.TrackOnClickListener
import com.example.playlistmaker.search.viewModel.SearchScreenState
import com.example.playlistmaker.search.viewModel.SearchViewModel
import com.example.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    companion object {
        private const val CHOICE_DEBOUNCE_DELAY = 1100L
    }

    private var tracksAdapter: TrackAdapter? = null
    private var binding: FragmentSearchBinding? = null
    private val searchViewModel by viewModel<SearchViewModel>()
    private lateinit var trackOnClickDebounced: (Track) -> Unit
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trackOnClickDebounced = debounce(
            CHOICE_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false
        ) { track ->
            searchViewModel.addTrackToHistory(track)
            searchViewModel.openTrack(track)
        }
        tracksAdapter = TrackAdapter(emptyList(), trackOnClickListener)
        binding?.clearSearchList?.setOnClickListener { searchViewModel.doClearHistory() }
        binding?.updateButton?.setOnClickListener { searchViewModel.doRepeatSearch() }
        searchBarTextWatcherAttach()
        searchBarSetActionDone()
        clearTextAttach()
        startUpViewHolder()
        searchViewModel.getScreenState().observe(viewLifecycleOwner) { render(it) }
    }

    private var trackOnClickListener = object : TrackOnClickListener {
        override fun onClick(item: Track) {
            trackOnClickDebounced(item)
        }
    }

    private fun render(screenState: SearchScreenState) {
        if (binding == null) return
        with(binding!!) {
            when (screenState) {
                is SearchScreenState.ResultHaveData -> {
                    progressBar.isVisible = false
                    beenSearchedTitle.isVisible = false
                    clearSearchList.isVisible = false
                    placeholderFrame.isVisible = false
                    if (tracksAdapter?.tracks != screenState.tracks) {
                        tracksAdapter?.tracks = screenState.tracks
                    }
                    tracksRecyclerView.visibility = View.VISIBLE
                }

                SearchScreenState.ResultIsEmpty -> {
                    progressBar.isVisible = false
                    beenSearchedTitle.isVisible = false
                    clearSearchList.isVisible = false
                    updateButton.isVisible = false
                    tracksRecyclerView.isVisible = false
                    statusText.text = this@SearchFragment.getString(R.string.search_status_nothing)
                    statusImage.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireActivity(), R.drawable.image_sad_smile_mus
                        )
                    )
                    placeholderFrame.visibility = View.VISIBLE
                }

                SearchScreenState.Error -> {
                    progressBar.isVisible = false
                    beenSearchedTitle.isVisible = false
                    clearSearchList.isVisible = false
                    tracksRecyclerView.isVisible = false
                    statusText.text =
                        requireActivity().getString(R.string.search_status_connection_problem)
                    statusImage.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireActivity(), R.drawable.image_no_wifi_mus
                        )
                    )
                    updateButton.visibility = View.VISIBLE
                    placeholderFrame.visibility = View.VISIBLE
                }

                is SearchScreenState.HistoryHaveData -> {
                    progressBar.isVisible = false
                    placeholderFrame.isVisible = false
                    updateButton.isVisible = false
                    if (tracksAdapter?.tracks != screenState.tracks) {
                        tracksAdapter?.tracks = screenState.tracks
                    }
                    beenSearchedTitle.visibility = View.VISIBLE
                    clearSearchList.visibility = View.VISIBLE
                    tracksRecyclerView.visibility = View.VISIBLE
                }

                SearchScreenState.HistoryIsEmpty -> {
                    progressBar.isVisible = false
                    placeholderFrame.isVisible = false
                    updateButton.isVisible = false
                    beenSearchedTitle.isVisible = false
                    clearSearchList.isVisible = false
                    tracksRecyclerView.isVisible = false
                }

                SearchScreenState.Loading -> {
                    placeholderFrame.isVisible = false
                    updateButton.isVisible = false
                    beenSearchedTitle.isVisible = false
                    clearSearchList.isVisible = false
                    tracksRecyclerView.isVisible = false
                    progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun searchBarSetActionDone() {
        binding?.searchBar?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchViewModel.doSearchTracks(binding?.searchBar?.text.toString())
            }
            false
        }
    }

    private fun clearTextAttach() {
        binding?.clearIcon?.setOnClickListener {
            binding?.searchBar?.setText("")
            requireActivity().currentFocus?.let { view ->
                val imm =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    private fun searchBarTextWatcherAttach() {
        val searchBarTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding?.clearIcon?.visibility = View.GONE
                    searchViewModel.cancelSearchSequence()
                } else {
                    binding?.clearIcon?.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty()) searchViewModel.doAutoSearchTracks(s.toString())
                else searchViewModel.showHistory()
            }
        }
        binding?.searchBar?.addTextChangedListener(searchBarTextWatcher)
    }

    private fun startUpViewHolder() {
        binding?.tracksRecyclerView?.adapter = tracksAdapter
        binding?.tracksRecyclerView?.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
    }
}