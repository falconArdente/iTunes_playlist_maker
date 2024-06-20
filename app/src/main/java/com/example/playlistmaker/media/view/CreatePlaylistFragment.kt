package com.example.playlistmaker.media.view

import android.content.res.Configuration
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.media.view.ui.CanShowPlaylistMessage
import com.example.playlistmaker.media.view.ui.FragmentCanShowDialog
import com.example.playlistmaker.media.view.ui.PlaylistMessage
import com.example.playlistmaker.media.viewModel.CreatePlaylistScreenState
import com.example.playlistmaker.media.viewModel.CreatePlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel


const val MESSAGE_TEXT = "message"
const val MESSAGE_DURATION = "duration"

open class CreatePlaylistFragment : Fragment(), FragmentCanShowDialog,
    CanShowPlaylistMessage {
    companion object {
        private const val FINISH_BY_DONE = "to_finish_by_done"

        fun createArgs(finishByDone: Boolean): Bundle {
            return bundleOf(FINISH_BY_DONE to finishByDone)
        }
    }

    private val mutableKeyboardState = MutableLiveData(false)
    protected open val viewModel: CreatePlaylistViewModel by viewModel<CreatePlaylistViewModel>()
    protected open var binding: FragmentCreatePlaylistBinding? = null
    private val viewKeyboardObserver = object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (binding == null) return
            val r = Rect()
            binding!!.root.getWindowVisibleDisplayFrame(r)
            val screenHeight: Int = binding!!.root.getRootView().height
            val keypadHeight = screenHeight - r.bottom
            if (keypadHeight > screenHeight * 0.15) { //Shown (0.15  seems good)
                if (mutableKeyboardState.value != true) mutableKeyboardState.postValue(true)
            } else {//Hidden
                if (mutableKeyboardState.value != false) mutableKeyboardState.postValue(false)
            }
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MASK_STATE)
        binding = FragmentCreatePlaylistBinding.inflate(layoutInflater, container, false)
        viewModel.attachFragmentAtCreation(this)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mutableKeyboardState.observe(viewLifecycleOwner) { keyboardDependentRender(it) }
        viewModel.finishActivityWhenDone = arguments?.getBoolean(FINISH_BY_DONE) ?: false
        viewModel.screenStateToObserve.observe(viewLifecycleOwner) { render(screenState = it) }
        viewModel.playlistMessageToObserve.observe(viewLifecycleOwner) { showMessage(it) }
        binding?.playlistImage?.setOnClickListener { viewModel.selectAnImage() }
        binding?.createButton?.setOnClickListener { viewModel.createPlaylist() }
        binding?.header?.setNavigationOnClickListener { viewModel.runExitSequence() }
        binding?.root?.viewTreeObserver?.addOnGlobalLayoutListener(viewKeyboardObserver)

        attachFieldsTextWatchers()
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )
    }

    private fun render(screenState: CreatePlaylistScreenState) {
        if (binding == null) return
        with(binding!!) {
            screenState.let { state ->
                if (state.imageUri != Uri.EMPTY) setImage(state.imageUri)
                if (state.title != title.text.toString()) title.setText(state.title)
                if (state.description != description.text.toString()) description.setText(state.description)
                setReadyToCreate(state.isReadyToSave)
            }
        }
    }

    private fun keyboardDependentRender(keyboardIsShown: Boolean) {
        if (binding == null || resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) return
        with(binding!!) {
            if (keyboardIsShown) {
                val fadeOut = AlphaAnimation(1f, 0f)
                fadeOut.duration = 90L
                playlistImage.startAnimation(fadeOut)
            } else {
                val fadeIn = AlphaAnimation(0f, 1f)
                fadeIn.duration = 90L
                playlistImage.startAnimation(fadeIn)
            }
            binding?.playlistImage?.isVisible = !keyboardIsShown
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
        if (binding == null) return
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

    override fun showDialog(dialog: MaterialAlertDialogBuilder) {
        dialog.show()
    }

    override fun showMessage(message: PlaylistMessage) {
        val activity = requireActivity()
        if (activity is CanShowPlaylistMessage) {
            activity.showMessage(message)
        } else return
    }
}