package com.example.playlistmaker.media.view

import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.media.view.ui.CanShowPlaylistMessage
import com.example.playlistmaker.media.view.ui.FragmentWithConfirmationDialog
import com.example.playlistmaker.media.view.ui.PlaylistMessage
import com.example.playlistmaker.media.viewModel.CreatePlaylistViewModel
import com.example.playlistmaker.media.viewModel.CreatePlaylistScreenState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

const val MESSAGE_TEXT = "message"
const val MESSAGE_DURATION = "duration"

open class CreatePlaylistFragment : Fragment(), FragmentWithConfirmationDialog,
    CanShowPlaylistMessage {
    companion object {
        private const val FINISH_BY_DONE = "to_finish_by_done"

        fun createArgs(finishByDone: Boolean): Bundle {
            return bundleOf(FINISH_BY_DONE to finishByDone)
        }
    }

    private val exitDialog: MaterialAlertDialogBuilder by lazy { configureExitConfirmationDialog() }
    protected open val viewModel by viewModel<CreatePlaylistViewModel>()
    protected open var binding: FragmentCreatePlaylistBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreatePlaylistBinding.inflate(layoutInflater, container, false)
        viewModel.attachFragmentAtCreation(this)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.finishActivityWhenDone = arguments?.getBoolean(FINISH_BY_DONE) ?: false
        viewModel.screenStateToObserve.observe(viewLifecycleOwner) { render(screenState = it) }
        viewModel.playlistMessageToObserve.observe(viewLifecycleOwner) { showMessage(it) }
        binding?.playlistImage?.setOnClickListener { viewModel.selectAnImage() }
        binding?.createButton?.setOnClickListener { viewModel.createPlaylist() }
        binding?.header?.setNavigationOnClickListener { viewModel.runExitSequence() }
        attachFieldsTextWatchers()
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )
    }

    private fun render(screenState: CreatePlaylistScreenState) {
        with(binding!!) {
            screenState.let { state ->
                if (state.imageUri != Uri.EMPTY) setImage(state.imageUri)
                if (state.title != title.text.toString()) title.setText(state.title)
                if (state.description != description.text.toString()) description.setText(state.description)
                setReadyToCreate(state.isReadyToSave)
            }
        }
    }

    private fun setImage(uri: Uri) {
        binding?.let {
            Glide.with(this).load(uri).fitCenter().centerCrop()
                .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.create_playlist_image_corner_radius)))
                .into(it.playlistImage)
        }
    }

    private fun setReadyToCreate(isReady: Boolean) {
        with(binding!!) {
            title.setBackgroundDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
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
        binding?.title?.doOnTextChanged { text, _, _, _ ->
            viewModel.changeTitle(text.toString())
        }
        binding?.description?.doOnTextChanged { text, _, _, _ ->
            viewModel.changeDescription(text.toString())
        }
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.runExitSequence()
        }
    }

    override fun runConfirmationDialog() {
        exitDialog.show()
    }

    private fun configureExitConfirmationDialog(): MaterialAlertDialogBuilder =
        MaterialAlertDialogBuilder(requireContext()).setBackground(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.dialog_background
            )
        )
            .setTitle(requireContext().getString(R.string.create_playlist_exit_dialog_title))
            .setMessage(R.string.create_playlist_exit_dialog_message)
            .setPositiveButton(requireContext().getString(R.string.create_playlist_exit_dialog_confirm)) { _, _ ->
                viewModel.exitView()
            }
            .setNegativeButton(requireContext().getString(R.string.create_playlist_exit_dialog_reject)) { dialogInterface: DialogInterface, _ ->
                dialogInterface.dismiss()
            }

    override fun showMessage(message: PlaylistMessage) {
        val activity = requireActivity()
        if (activity is CanShowPlaylistMessage) {
            activity.showMessage(message)
        } else return
    }
}