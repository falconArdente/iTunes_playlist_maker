package com.example.playlistmaker.settings.ui

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.main.ui.MainActivity


class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        backButtonAttach()
        switchListenerAttach()
        contactSupportAttach()
        viewAgreementAttach()
        shareAnAppAttach()
    }

    private fun shareAnAppAttach() {
        binding.shareAnApp.setOnClickListener {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name)
                val shareMessage = "\n${R.string.to_share_text}\n\n" +
                        applicationContext.getString(R.string.playmarket_URL_base) + applicationInfo.uid
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage.trimIndent())
                startActivity(shareIntent)
            } catch (e: Exception) {
                Log.e("SHARE", e.message.toString())
            }
        }
    }

    private fun backButtonAttach() {
        val goMain = Intent(this, MainActivity::class.java)
        binding.header.setNavigationOnClickListener { startActivity(goMain) }
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

    private fun contactSupportAttach() {
        binding.emailToSupport.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/html"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(R.string.my_email))
            intent.putExtra(Intent.EXTRA_SUBJECT, R.string.email_subject_for_support)
            intent.putExtra(Intent.EXTRA_TEXT, R.string.email_body_for_support)
            startActivity(intent)
        }
    }

    private fun viewAgreementAttach() {
        binding.viewAgreement.setOnClickListener {
            val url: String = resources.getString(R.string.agreement_URL)
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        }
    }
}