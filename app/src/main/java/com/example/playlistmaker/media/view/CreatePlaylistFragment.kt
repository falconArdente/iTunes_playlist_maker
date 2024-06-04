package com.example.playlistmaker.media.view

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.media.model.data.storage.PrivateStorageImageRepositoryImpl
import com.example.playlistmaker.media.view.ui.ImageSelectionRepositoryPhotoPickerBased
import com.example.playlistmaker.media.viewModel.CreatePlaylistViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistFragment : Fragment() {

    companion object {
        fun newInstance() = CreatePlaylistFragment()
    }

    private val viewModel by viewModel<CreatePlaylistViewModel>()
    private lateinit var _binding: FragmentCreatePlaylistBinding
    private val binding: FragmentCreatePlaylistBinding
        get() = _binding
    private val repo by inject<ImageSelectionRepositoryPhotoPickerBased>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.playlistImage.setOnClickListener {
           lifecycleScope.launch {
               repo.pickAnImage()
                   .collect{
                       binding.playlistImage.setImageURI(it)
                       saveImageToPrivateStorage(it)
                   }
           }
        }
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val repoForSave=PrivateStorageImageRepositoryImpl(requireActivity())
        repoForSave.saveImageByUri(uri,"Test")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePlaylistBinding.inflate(layoutInflater, container, false)
        repo.attachPickerToFragmentLifecycle(this)
        return binding.root
    }
}