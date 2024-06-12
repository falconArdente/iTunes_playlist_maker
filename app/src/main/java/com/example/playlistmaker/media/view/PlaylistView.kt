package com.example.playlistmaker.media.view

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.playlistmaker.databinding.FragmentPlaylistItemViewLandscapeBinding
import com.example.playlistmaker.databinding.FragmentPlaylistItemViewPortraitBinding
import com.example.playlistmaker.media.viewModel.PlaylistViewViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistView : Fragment() {

    companion object {
        fun newInstance() = PlaylistView()
    }

    private val viewModel by viewModel<PlaylistViewViewModel>()
    private var isPortrait: Boolean = true
    private var binding: ViewBinding? = null
        get() {
            return if (isPortrait) field as FragmentPlaylistItemViewPortraitBinding
            else field as FragmentPlaylistItemViewLandscapeBinding
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isPortrait = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        binding =
            if (isPortrait)
                FragmentPlaylistItemViewPortraitBinding.inflate(inflater, container, false)
            else
                FragmentPlaylistItemViewLandscapeBinding.inflate(inflater, container, false)

        return binding!!.root
    }
}