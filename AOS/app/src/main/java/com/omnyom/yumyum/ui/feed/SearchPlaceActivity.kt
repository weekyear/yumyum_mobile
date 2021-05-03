package com.omnyom.yumyum.ui.feed

import android.Manifest
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.ActivitySearchPlaceBinding
import com.omnyom.yumyum.helper.KakaoMapUtils
import com.omnyom.yumyum.helper.KakaoMapUtils.Companion.PERM_FINE_LOCATION
import com.omnyom.yumyum.helper.recycler.SearchPlaceResultsAdapter
import com.omnyom.yumyum.ui.base.BaseBindingActivity

class SearchPlaceActivity : BaseBindingActivity<ActivitySearchPlaceBinding> (R.layout.activity_search_place) {

    companion object {
        const val PLACE_CODE = 11
    }

    private val searchPlaceVM: SearchPlaceViewModel by viewModels()

    override fun extraSetupBinding() {
    }

    override fun setup() {
        KakaoMapUtils.initLocationManager(this)
        requirePermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERM_FINE_LOCATION)
    }

    override fun setupViews() {
        supportActionBar?.hide()
        binding.rvPlace.apply {
            val _adapter = SearchPlaceResultsAdapter(this@SearchPlaceActivity).apply {
                setVM(searchPlaceVM)
            }
            adapter = _adapter
            layoutManager = LinearLayoutManager(this@SearchPlaceActivity)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchPlaceVM.searchPlace(query!!)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }


    override fun onSubscribe() {
        searchPlaceVM.searchPlaceResults.observe(this, {
            val adapter = binding.rvPlace.adapter as SearchPlaceResultsAdapter
            adapter.run {
                setItems(it)
                notifyDataSetChanged()
            }
        })
    }

    override fun release() {
    }


}