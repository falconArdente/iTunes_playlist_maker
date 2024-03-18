package com.example.playlistmaker.settings.ui

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.viewModel.SettingsViewModel


class SettingsActivity : ComponentActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("sett-s","starting")
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("sett-s","inflated")
        settingsViewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]
        Log.d("sett-s","didCreateAVM")
        settingsViewModel.getThemeSwitchState().observe(this) { render(it) }
        binding.header.setNavigationOnClickListener {finish()}
        binding.isNightThemeSwitch.setOnClickListener { settingsViewModel.doSwitchTheThemeState() }
        binding.emailToSupport.setOnClickListener {settingsViewModel.emailToSupport()}
        binding.viewAgreement.setOnClickListener{settingsViewModel.goToAgreement()}
        binding.shareAnApp.setOnClickListener{settingsViewModel.doShareAnApp()}
    }
private fun render(isDarkTheme:Boolean){

}
    private fun switchListenerAttach() {
        binding.isNightThemeSwitch.setOnCheckedChangeListener { _, isChecked ->
            (applicationContext as App).switchTheme(isChecked)
        }
    }

    override fun onResume() {
        super.onResume()
        val isNightModeOutside =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        binding.isNightThemeSwitch.isChecked = isNightModeOutside
    }

}