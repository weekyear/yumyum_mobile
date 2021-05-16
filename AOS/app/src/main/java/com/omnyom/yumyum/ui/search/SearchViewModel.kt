package com.omnyom.yumyum.ui.search

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.omnyom.yumyum.helper.PreferencesManager
import com.omnyom.yumyum.helper.PreferencesManager.Companion.userId
import com.omnyom.yumyum.helper.RetrofitManager.Companion.retrofitService
import com.omnyom.yumyum.model.maps.SearchPlaceResult
import com.omnyom.yumyum.model.search.SearchFeedData
import com.omnyom.yumyum.model.search.SearchFeedListResponse
import com.omnyom.yumyum.model.search.SearchPlaceData
import com.omnyom.yumyum.model.search.SearchPlaceListResponse
import com.omnyom.yumyum.ui.base.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel(application: Application) : BaseViewModel(application) {

    private val _searchFeedResults = MutableLiveData<List<SearchFeedData>>().apply {
        value = arrayListOf()
    }
    val searchFeedResults : LiveData<List<SearchFeedData>> = _searchFeedResults

    var isSearched : Boolean = false

    fun searchFeed(searchText: String) {
        var call = retrofitService.getSearchFeedListByTitle(searchText, userId)
        call.enqueue(object : Callback<SearchFeedListResponse> {
            override fun onResponse(call: Call<SearchFeedListResponse>, response: Response<SearchFeedListResponse>) {
                if (response.isSuccessful) {
                    val list : List<SearchFeedData> = response.body()?.data!!
                    _searchFeedResults.postValue(list)
                }
            }

            override fun onFailure(call: Call<SearchFeedListResponse>, t: Throwable) {
                t
            }
        })
    }

    private val _searchPlaceResults = MutableLiveData<List<SearchPlaceData>>().apply {
        value = arrayListOf()
    }
    val searchPlaceResults : LiveData<List<SearchPlaceData>> = _searchPlaceResults

    fun searchPlace(searchText: String) {
        var call = retrofitService.getSearchPlaceList("name", searchText)
        call.enqueue(object : Callback<SearchPlaceListResponse> {
            override fun onResponse(call: Call<SearchPlaceListResponse>, response: Response<SearchPlaceListResponse>) {
                if (response.isSuccessful) {
                    val list : List<SearchPlaceData> = response.body()?.data!!
                    _searchPlaceResults.postValue(list)
                }
            }

            override fun onFailure(call: Call<SearchPlaceListResponse>, t: Throwable) {
                t
            }

        })
    }
}