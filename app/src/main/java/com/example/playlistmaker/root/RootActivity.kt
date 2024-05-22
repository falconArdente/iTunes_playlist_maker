package com.example.playlistmaker.root

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.media.model.data.db.AppDbRoomBased
import com.example.playlistmaker.media.model.data.db.TrackDbConverter
import com.example.playlistmaker.utils.testTrack_1
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class RootActivity : AppCompatActivity(R.layout.activity_root) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //!!!!!!!!!!!!!!!!!!!!!!!!
        val db by inject<AppDbRoomBased>()
        lifecycle.coroutineScope.launch {
               db.favoriteTracksDao().addTrackEntity(TrackDbConverter.map(testTrack_1))
        }
//!!!!!!!!!!!!!!!!!!!!!!!!
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootFragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomMenu = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        val borderLine = findViewById<View>(R.id.bottom_views_border)
        bottomMenu.setupWithNavController(navController)
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
}