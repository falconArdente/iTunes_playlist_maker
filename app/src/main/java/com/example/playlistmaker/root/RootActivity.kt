package com.example.playlistmaker.root

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.media.view.ui.CanShowPlaylistMessage
import com.example.playlistmaker.media.view.ui.PlaylistMessage
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val FRAGMENT_LOAD_COMMAND = "load_fragment"
const val FINISH_BY_DONE = "to_finish_by_done"

class RootActivity : AppCompatActivity(R.layout.activity_root), CanShowPlaylistMessage {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootFragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomMenu = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        val borderLine = findViewById<View>(R.id.bottom_views_border)
        bottomMenu.setupWithNavController(navController)
        unpackAnIntent(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.searchFragment, R.id.mediaFragment, R.id.settingsFragment -> {
                    bottomMenu.isGone = false
                    borderLine.isGone = false
                }

                else -> {
                    bottomMenu.isGone = true
                    borderLine.isGone = true
                }
            }
        }
    }

    private fun unpackAnIntent(navController: NavController) {
        val fragmentToNavigate = intent.getIntExtra(FRAGMENT_LOAD_COMMAND, 0)

        if (fragmentToNavigate > 0) {
            val args = Bundle()
            args.putBoolean(FINISH_BY_DONE, true)
            navController.navigate(fragmentToNavigate, args = args, navOptions = navOptions {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            })
        }
    }

    override fun showMessage(message: PlaylistMessage) {
        if (message is PlaylistMessage.Empty) return
        val messenger = findViewById<LinearLayout>(R.id.playlist_messenger)
        val textView = findViewById<TextView>(R.id.playlist_message_text_view)
        val bottomMenu = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        val borderLine = findViewById<View>(R.id.bottom_views_border)
        with((message as PlaylistMessage.HaveData)) {
            lifecycleScope.launch {
                var bottomState = bottomMenu.isVisible
                textView.text = this@with.message
                bottomMenu.isVisible = false
                borderLine.isVisible = false
                messenger.isVisible = true
                delay(this@with.showTimeMillis)
                bottomMenu.isVisible = bottomState
                borderLine.isVisible = bottomState
                messenger.isVisible = false
            }
        }
    }
}