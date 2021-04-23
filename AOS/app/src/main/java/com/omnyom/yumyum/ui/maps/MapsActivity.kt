package com.omnyom.yumyum.ui.maps

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.omnyom.yumyum.MainActivity
import com.omnyom.yumyum.databinding.ActivityMapsBinding
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import net.daum.mf.map.n.api.internal.NativeMapLocationManager.*

class MapsActivity : AppCompatActivity() {

    val binding by lazy { ActivityMapsBinding.inflate(layoutInflater) }
    val PERMISSIONS_REQUEST_CODE = 100
    var REQUIRED_PERMISSIONS = arrayOf<String>( Manifest.permission.ACCESS_FINE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var mapViewInput = MapView(this)
        mapViewInput.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving)
        mapViewInput.setShowCurrentLocationMarker(true)
        binding.mapView.addView(mapViewInput)



        binding.btnGetLocation.setOnClickListener {
            val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            if(permissionCheck == PackageManager.PERMISSION_GRANTED) {
                val lm: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                try {
                    val userNowLocation: Location? =
                        lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    val uLatitude = userNowLocation?.latitude
                    val uLongitude = userNowLocation?.longitude
                    val uNowPosition = MapPoint.mapPointWithGeoCoord(uLatitude?:0.0, uLongitude?:0.0)
                    mapViewInput.setMapCenterPoint(uNowPosition, true)
                }catch(e: NullPointerException){
                    Log.e("LOCATION_ERROR", e.toString())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        ActivityCompat.finishAffinity(this)
                    }else{
                        ActivityCompat.finishAffinity(this)
                    }

                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                    System.exit(0)
                }
            }else{
                Toast.makeText(this, "위치 권한이 없습니다.", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE )
            }
        }

    }
}