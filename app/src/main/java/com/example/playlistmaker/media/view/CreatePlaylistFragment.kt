package com.example.playlistmaker.media.view

import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.media.view.ui.FragmentWithExitConfirmationDialog
import com.example.playlistmaker.media.viewModel.CreatePlaylistScreenState
import com.example.playlistmaker.media.viewModel.CreatePlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistFragment : Fragment(), FragmentWithExitConfirmationDialog {

    companion object {
        fun newInstance() = CreatePlaylistFragment()
    }

    private val exitDialog: MaterialAlertDialogBuilder by lazy { configureExitConfirmationDialog() }
    private val viewModel by viewModel<CreatePlaylistViewModel>()
    private lateinit var _binding: FragmentCreatePlaylistBinding
    private val binding: FragmentCreatePlaylistBinding
        get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePlaylistBinding.inflate(layoutInflater, container, false)
        viewModel.attachFragmentAtCreation(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.screenStateToObserve.observe(this) { render(screenState = it) }
        binding.playlistImage.setOnClickListener { viewModel.selectAnImage() }
        binding.createButton.setOnClickListener { viewModel.createPlaylist() }
        binding.header.setNavigationOnClickListener { viewModel.exitSequence() }
        attachFieldsTextWatchers()
        requireActivity().onBackPressedDispatcher.addCallback(backPressedCallback)
    }

    private fun render(screenState: CreatePlaylistScreenState) {
        with(binding) {
            screenState.let { state ->
                if (state.imageUri != Uri.EMPTY) setImage(state.imageUri)
                if (state.title != title.text.toString()) title.setText(state.title)
                if (state.description != description.text.toString()) description.setText(state.description)
                setReadyToCreate(state.isReadyToCreate)
            }
        }
    }

    private fun setImage(uri: Uri) {
        Glide.with(this).load(uri).fitCenter().centerCrop()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.create_playlist_image_corner_radius)))
            .into(binding.playlistImage)
    }

    private fun setReadyToCreate(isReady: Boolean) {
        with(binding) {
            title.setBackgroundDrawable(
                requireActivity().getDrawable(
                    if (isReady) R.drawable.create_playlist_field_active_background
                    else R.drawable.create_playlist_field_init_background
                )
            )
            description.setBackgroundDrawable(title.background)
            titleUpString.isVisible = isReady
            descriptionUpString.isVisible = isReady
            createButton.isEnabled = isReady
        }
    }


    private fun attachFieldsTextWatchers() {
        val titleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.changeTitle(s.toString())
            }
        }
        val descTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.changeDescription(s.toString())
            }
        }
        val titleOnFocus = OnFocusChangeListener { v, hasFocus ->
            when (hasFocus) {
                true -> {
                    if ((v as EditText).text.toString() == requireActivity().getString(R.string.create_playlist_title_field)) v.setText(
                        ""
                    )
                }

                false -> {
                    if ((v as EditText).text.toString() == "") {
                        v.setText(
                            requireActivity().getString(R.string.create_playlist_title_field)
                        )
                    }
                }
            }
        }
        val descOnFocus = OnFocusChangeListener { v, hasFocus ->
            when (hasFocus) {
                true -> {
                    if ((v as EditText).text.toString() == requireActivity().getString(R.string.create_playlist_description_field)) v.setText(
                        ""
                    )
                }

                false -> {
                    if ((v as EditText).text.toString() == "") {
                        v.setText(
                            requireActivity().getString(R.string.create_playlist_description_field)
                        )
                    }
                }
            }
        }
        with(binding) {
            title.addTextChangedListener(titleTextWatcher)
            title.onFocusChangeListener = titleOnFocus
            description.addTextChangedListener(descTextWatcher)
            description.onFocusChangeListener = descOnFocus
        }
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.exitSequence()
        }
    }

    override fun runExitConfirmationDialog() {
        exitDialog.show()
    }

    private fun configureExitConfirmationDialog(): MaterialAlertDialogBuilder =
        MaterialAlertDialogBuilder(requireContext()).setBackground(requireContext().getDrawable(R.drawable.dialog_background))
            .setTitle(requireContext().getString(R.string.create_playlist_exit_dialog_title))
            .setMessage(R.string.create_playlist_exit_dialog_message)
            .setPositiveButton(requireContext().getString(R.string.create_playlist_exit_dialog_confirm)) { dialogInterface: DialogInterface, _ ->
                findNavController().navigateUp()
                dialogInterface.dismiss()
            }
            .setNegativeButton(requireContext().getString(R.string.create_playlist_exit_dialog_reject)) { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            }
}