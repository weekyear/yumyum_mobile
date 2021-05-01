package com.omnyom.yumyum.ui.feed

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.omnyom.yumyum.helper.KakaoMapUtils
import com.omnyom.yumyum.helper.RetrofitManager
import com.omnyom.yumyum.kakaoApi
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.model.maps.KeywordSearchResponse
import com.omnyom.yumyum.model.maps.SearchPlaceResult
import com.omnyom.yumyum.ui.base.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchPlaceViewModel(application: Application) : BaseViewModel(application) {
    private val _searchPlaceResults = MutableLiveData<List<SearchPlaceResult>>().apply {
        value = ArrayList()
    }
    val searchPlaceResults : LiveData<List<SearchPlaceResult>> = _searchPlaceResults

    fun searchPlace(searchText: String) {
        val positions = KakaoMapUtils.getMyPosition(getApplication())
        var call = RetrofitManager.kakaoApiService.placeSearch(kakaoApi.API_KEY, searchText, positions[0], positions[1], 1, 7)
        call.enqueue(object : Callback<KeywordSearchResponse> {
            override fun onResponse(call: Call<KeywordSearchResponse>, response: Response<KeywordSearchResponse>) {
                if (response.isSuccessful) {
                    val list : List<SearchPlaceResult> = response.body()?.documents!!
                    _searchPlaceResults.postValue(list)
                }
            }

            override fun onFailure(call: Call<KeywordSearchResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}