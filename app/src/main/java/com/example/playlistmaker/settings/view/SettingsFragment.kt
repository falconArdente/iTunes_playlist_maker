package com.example.playlistmaker.settings.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.settings.model.domain.ThemeState
import com.example.playlistmaker.settings.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    companion object {
        private const val THEME_SWITCH_DEBOUNCE_DELAY: Long = 270L
    }

    private lateinit var binding: FragmentSettingsBinding
    private val settingsViewModel: SettingsViewModel by viewModel()
    private val handler = Handler(Looper.getMainLooper())
    private var tempThemeForRunnable: ThemeState = ThemeState.NIGHT_THEME

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("settFrag","binding")
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("settFrag","created")

        settingsViewModel.getThemeSwitchState().observe(viewLifecycleOwner) { render(it) }
        binding.emailToSupport.setOnClickListener { settingsViewModel.emailToSupport() }
        binding.viewAgreement.setOnClickListener { settingsViewModel.goToAgreement() }
        binding.shareAnApp.setOnClickListener { settingsViewModel.doShareAnApp() }
        binding.isNightThemeSwitch.setOnCheckedChangeListener(onCheckedChangeListener)
    }

    private fun render(themeState: ThemeState) {
        binding.isNightThemeSwitch.isChecked = themeState == ThemeState.NIGHT_THEME
        binding.isNightThemeSwitch.isEnabled = true
    }

    private val delayedThemeSwitch = object : Runnable {
        override fun run() {
            handler.removeCallbacks(this)
            settingsViewModel.doSwitchTheThemeState(tempThemeForRunnable)
            binding.isNightThemeSwitch.isEnabled = true
        }
    }
    private val onCheckedChangeListener =
        CompoundButton.OnCheckedChangeListener { _, isChecked ->
            binding.isNightThemeSwitch.isEnabled = false
            tempThemeForRunnable = if (isChecked) ThemeState.NIGHT_THEME else ThemeState.DAY_THEME
            handler.postDelayed(delayedThemeSwitch, THEME_SWITCH_DEBOUNCE_DELAY)
        }
}