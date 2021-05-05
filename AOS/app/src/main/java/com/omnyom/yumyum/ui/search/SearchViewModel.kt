package com.omnyom.yumyum.ui.search

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.omnyom.yumyum.helper.RetrofitManager.Companion.retrofitService
import com.omnyom.yumyum.model.maps.SearchPlaceResult
import com.omnyom.yumyum.model.place.SearchPlaceData
import com.omnyom.yumyum.model.place.SearchPlaceListResponse
import com.omnyom.yumyum.ui.base.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel(application: Application) : BaseViewModel(application) {

    private val _searchPlaceResults = MutableLiveData<List<SearchPlaceData>>().apply {
        value = arrayListOf(SearchPlaceData(
                "dd",
                0.1,
                0.2,
                "ddjj",
                "0101--1-32-",
                99
        ))
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