package com.omnyom.yumyum

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.omnyom.yumyum.databinding.ActivityMainBinding
import com.omnyom.yumyum.helper.KakaoMapUtils
import com.omnyom.yumyum.helper.PreferencesManager
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
        supportActionBar?.hide()

        val userId = PreferencesManager.getLong(this, "userId")

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_profile))
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        binding.btnCreateFeed.setOnClickListener { startActivity(cameraIntent) }
        binding.btnMainMap.setOnClickListener { startActivity(mapIntent) }

        binding.animationView.setMaxFrame(15)
        binding.animationView2.setMinFrame(15)
        if (!binding.animationView.isAnimating && !binding.animationView2.isAnimating) {
            binding.animationView.setOnClickListener { icon1Click() }
            binding.animationView2.setOnClickListener { icon2Click() }
        }




    }

    fun icon1Click() {
        Log.d("icon", "1번마")
        val userId = PreferencesManager.getLong(this, "userId")
        binding.animationView.playAnimation()
        Handler().postDelayed({
            binding.animationView.visibility = View.INVISIBLE
            binding.animationView2.visibility = View.VISIBLE
            binding.animationView.progress = 0.0f

        }, 800)
    }

    fun icon2Click() {
        Log.d("icon", "2번마")
        val userId = PreferencesManager.getLong(this, "userId")
        binding.animationView2.playAnimation()
        Handler().postDelayed({
            binding.animationView2.visibility = View.INVISIBLE
            binding.animationView.visibility = View.VISIBLE
            binding.animationView2.progress = 0.5f
        }, 800)
    }


}
