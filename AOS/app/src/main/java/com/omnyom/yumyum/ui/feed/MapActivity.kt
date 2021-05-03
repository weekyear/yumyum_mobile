package com.omnyom.yumyum.ui.feed

import android.Manifest
import android.widget.Toast
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.ActivityMapBinding
import com.omnyom.yumyum.helper.KakaoMapUtils
import com.omnyom.yumyum.helper.KakaoMapUtils.Companion.PERM_FINE_LOCATION
import com.omnyom.yumyum.model.maps.SearchPlaceResult
import com.omnyom.yumyum.ui.base.BaseBindingActivity
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MapActivity : BaseBindingActivity<ActivityMapBinding>(R.layout.activity_map) {
    lateinit var x: String
    lateinit var y: String

    private lateinit var placeResult: SearchPlaceResult

    override fun extraSetupBinding() {
    }

    override fun setup() {
        requirePermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERM_FINE_LOCATION)
        placeResult = intent.getSerializableExtra("placeResult") as SearchPlaceResult
    }

    override fun setupViews() {
        var mapViewInput = MapView(this).apply {
            currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving
            setShowCurrentLocationMarker(true)
            val mapPoint = MapPoint.mapPointWithGeoCoord(placeResult.y.toDouble(), placeResult.x.toDouble())
            setMapCenterPointAndZoomLevel(mapPoint, 0, true)

            addPOIItem(getMarker(mapPoint, placeResult.place_name))
        }
        binding.mapView.addView(mapViewInput)

        binding.btnMyLocation.setOnClickListener {
            val myPosition = KakaoMapUtils.getMyPosition(this)
            val uNowPosition = MapPoint.mapPointWithGeoCoord(myPosition[1]?:0.0, myPosition[0]?:0.0)
            mapViewInput.setMapCenterPoint(uNowPosition, true)
        }

        supportActionBar?.hide()
    }

    override fun onSubscribe() { }

    override fun release() { }


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
        }
    }


    private fun getMarker(mapPoint: MapPoint, placeName: String): MapPOIItem {
        return MapPOIItem().apply {
            itemName = placeResult.place_name
            tag = 0
            this.mapPoint = mapPoint
            markerType = MapPOIItem.MarkerType.YellowPin
            selectedMarkerType = MapPOIItem.MarkerType.RedPin
        }
    }
}