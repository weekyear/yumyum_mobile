package com.omnyom.yumyum

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.omnyom.yumyum.databinding.ActivityMainBinding
import com.omnyom.yumyum.ui.feed.CameraActivity
import com.omnyom.yumyum.ui.maps.MapsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val cameraIntent = Intent(this, CameraActivity::class.java)
        val mapIntent = Intent(this, MapsActivity::class.java)


        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_profile))
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        binding.btnCreateFeed.setOnClickListener { startActivity(cameraIntent) }
        binding.btnMaps.setOnClickListener { startActivity(mapIntent) }
    }


}
