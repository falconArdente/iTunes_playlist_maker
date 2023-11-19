package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        backButtonAttach()
        switchListenerAttach()
        contactSupportAttach()
        viewAgreementAttach()
        shareAnAppAttach()
    }

    private fun shareAnAppAttach() {
        val shareButton = findViewById<LinearLayout>(R.id.share_an_app)
        shareButton.setOnClickListener {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name)
                val shareMessage = "\n${R.string.to_share_text}\n\n" +
                        "$applicationContext.getString(R.string.playmarket_URL_base)" + applicationInfo.uid
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage.trimIndent())
                startActivity(shareIntent)
            } catch (e: Exception) {
                Log.e("SHARE", e.message.toString())
            }
        }
    }

    private fun backButtonAttach() {
        val header = findViewById<androidx.appcompat.widget.Toolbar>(R.id.header)
        val goMain = Intent(this, MainActivity::class.java)
        header.setNavigationOnClickListener { startActivity(goMain) }
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private fun switchListenerAttach() {
        val darkThemeSwitch = findViewById<Switch>(R.id.is_night_theme_switch)
        darkThemeSwitch.setOnClickListener {
            val isNightModeOutside =
                resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
            if (darkThemeSwitch.isChecked != isNightModeOutside) {
                if (darkThemeSwitch.isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onResume() {
        super.onResume()
        val darkThemeSwitch = findViewById<Switch>(R.id.is_night_theme_switch)
        val isNightModeOutside =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        darkThemeSwitch.isChecked = isNightModeOutside
    }

    private fun contactSupportAttach() {
        val contactToSupportButton = findViewById<LinearLayout>(R.id.email_to_support)
        contactToSupportButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/html"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(R.string.my_email))
            intent.putExtra(Intent.EXTRA_SUBJECT, R.string.email_subject_for_support)
            intent.putExtra(Intent.EXTRA_TEXT, R.string.email_body_for_support)
            startActivity(intent)
        }
    }

    private fun viewAgreementAttach() {
        val viewAgreementButton = findViewById<LinearLayout>(R.id.view_agreement)
        viewAgreementButton.setOnClickListener {
            val url: String = resources.getString(R.string.agreement_URL)
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        }
    }

}