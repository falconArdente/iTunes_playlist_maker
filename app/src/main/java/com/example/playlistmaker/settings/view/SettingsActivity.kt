package com.example.playlistmaker.settings.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.model.domain.ThemeState
import com.example.playlistmaker.settings.viewModel.SettingsViewModel

class SettingsActivity : AppCompatActivity() {
    companion object {
        private const val THEME_SWITCH_DEBOUNCE_DELAY: Long = 270L
    }

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var handler: Handler
    private var tempThemeForRunnable: ThemeState = ThemeState.NIGHT_THEME
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handler = Handler(Looper.getMainLooper())
        settingsViewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]
        settingsViewModel.getThemeSwitchState().observe(this) { render(it) }
        binding.header.setNavigationOnClickListener { finish() }
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