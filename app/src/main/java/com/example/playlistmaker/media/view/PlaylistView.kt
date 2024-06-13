package com.example.playlistmaker.media.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentPlaylistItemViewBinding
import com.example.playlistmaker.media.viewModel.PlaylistViewViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistView : Fragment() {

    companion object {
        fun newInstance() = PlaylistView()
    }

    private val viewModel by viewModel<PlaylistViewViewModel>()
    private var binding: FragmentPlaylistItemViewBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistItemViewBinding.inflate(inflater, container, false)

        return binding!!.root
    }
}