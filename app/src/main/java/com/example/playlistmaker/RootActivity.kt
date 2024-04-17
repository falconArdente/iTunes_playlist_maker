package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class RootActivity : AppCompatActivity(R.layout.activity_root) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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