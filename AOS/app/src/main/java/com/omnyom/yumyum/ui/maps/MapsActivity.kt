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
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.omnyom.yumyum.BaseActivity
import com.omnyom.yumyum.KakaoRetrofitBuilder
import com.omnyom.yumyum.databinding.ActivityMapsBinding
import com.omnyom.yumyum.interfaces.KakaoApiService
import com.omnyom.yumyum.kakaoApi
import com.omnyom.yumyum.model.maps.Document
import com.omnyom.yumyum.model.maps.KeywordSearchResponse
import com.omnyom.yumyum.ui.feed.LocationListActivity
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import retrofit2.*
import java.io.Serializable

class MapsActivity : BaseActivity() {

    val binding by lazy { ActivityMapsBinding.inflate(layoutInflater) }
    lateinit var x: String
    lateinit var y: String
    val PERMISSIONS_REQUEST_CODE = 110
    var REQUIRED_PERMISSIONS = arrayOf<String>( Manifest.permission.ACCESS_FINE_LOCATION)
    val PERM_FINE_LOCATION = 110 // 외부 저장소 권한 처리
    val PERM_COARSE_LOCATION = 111 // 카메라 권한처리



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        requirePermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), PERM_COARSE_LOCATION)
        requirePermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERM_FINE_LOCATION)

        binding.inputPlaceName.addTextChangedListener {  }
        var mapViewInput = MapView(this)

        mapViewInput.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving)
        mapViewInput.setShowCurrentLocationMarker(true)
        binding.mapView.addView(mapViewInput)


        binding.inputPlaceName.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(textView: TextView, actionId: Int, event: KeyEvent?): Boolean {
                when(actionId) {
                    EditorInfo.IME_ACTION_SEARCH -> Search()
                }
                return false // return true to consume event and prevent keyboard from disappearing
            }
        })


        binding.btnGetLocation.setOnClickListener {
            val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                val lm: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                try {
                    val userNowLocation: Location? =
                        lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    val uLatitude = userNowLocation?.latitude
                    val uLongitude = userNowLocation?.longitude
                    x = uLongitude.toString()
                    y = uLatitude.toString()
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


    override fun permissionGranted(requestCode: Int) {
        when (requestCode) {
        }
    }

    override fun permissionDenied(requestCode: Int) {
        when (requestCode) {
            PERM_FINE_LOCATION -> {
                Toast.makeText(baseContext, "위치 권한을 승인해야 지도를 사용할 수 있습니다!?", Toast.LENGTH_SHORT).show()
                finish()
            }
            PERM_COARSE_LOCATION -> {
                Toast.makeText(baseContext, "위치 권한을 승인해야 지도를 사용할 수 있습니다!?", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun Search() {
        var kakaoApiService: KakaoApiService = KakaoRetrofitBuilder.buildService(KakaoApiService::class.java)
        var call = kakaoApiService.placeSearch(kakaoApi.API_KEY, binding.inputPlaceName.text.toString(), x.toDouble(), y.toDouble(), 1, 5)
        call.enqueue(object : Callback<KeywordSearchResponse> {
            override fun onResponse(call: Call<KeywordSearchResponse>, response: Response<KeywordSearchResponse>) {
                if (response.isSuccessful) {
                    val intent = Intent(this@MapsActivity, LocationListActivity::class.java)
                    val list : List<Document> = response.body()?.documents!!
                    intent.putExtra("DocumentList", list as Serializable)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<KeywordSearchResponse>, t: Throwable) {
                t
                TODO("Not yet implemented")
            }

        })
    }
}
