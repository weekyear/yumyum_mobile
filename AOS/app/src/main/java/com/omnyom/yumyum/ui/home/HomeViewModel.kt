package com.omnyom.yumyum.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omnyom.yumyum.TempRetrofitBuilder
import com.omnyom.yumyum.helper.PreferencesManager
import com.omnyom.yumyum.helper.RetrofitManager.Companion.retrofitService
import com.omnyom.yumyum.interfaces.RetrofitService
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.model.feed.FeedResponse
import com.omnyom.yumyum.model.place.GetPlaceDataResponse
import com.omnyom.yumyum.ui.base.BaseViewModel

import retrofit2.*

class HomeViewModel(application: Application) : BaseViewModel(application) {

    init {
        val userId = PreferencesManager.getLong(getApplication(), "userId")
        var call = retrofitService.getAllFeeds(userId!!)
        call.enqueue(object : Callback<FeedResponse> {
            override fun onResponse(call: Call<FeedResponse>, response: Response<FeedResponse>) {
                if (response.isSuccessful) {
                    _foodData.postValue(response.body()?.data!!.toMutableList().reversed())
                }
            }

            override fun onFailure(call: Call<FeedResponse>, t: Throwable) {
                t
            }

        })
    }

    // FoodList를 LiveData 객채로 생성
    private val _foodData = MutableLiveData<List<FeedData>>().apply {
    }
    val foodData : LiveData<List<FeedData>> = _foodData



}

