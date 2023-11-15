package com.example.playlistmaker


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
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
    }

    private fun backButtonAttach() {
        val backButton = findViewById<Button>(R.id.back_button)
        val goMain = Intent(this, MainActivity::class.java)
        backButton.setOnClickListener { startActivity(goMain) }
    }

    private fun switchListenerAttach() {
        val darkThemeSwitch = findViewById<Switch>(R.id.is_night_theme_switch)
        darkThemeSwitch.setOnClickListener {
            if (darkThemeSwitch.isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val darkThemeSwitch = findViewById<Switch>(R.id.is_night_theme_switch)
        darkThemeSwitch.isChecked =
            AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
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