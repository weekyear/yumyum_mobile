package com.omnyom.yumyum.ui.myinfo

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omnyom.yumyum.TempRetrofitBuilder
import com.omnyom.yumyum.interfaces.RetrofitService
import com.omnyom.yumyum.model.feed.AllFeedResponse
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.ui.base.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFeedViewModel(application: Application) : BaseViewModel(application) {
    private var myRetrofitService: RetrofitService = TempRetrofitBuilder.buildService(RetrofitService::class.java)

    init {
        var call = myRetrofitService.getUserFeeds(10, 10)
        call.enqueue(object : Callback<AllFeedResponse> {
            override fun onResponse(call: Call<AllFeedResponse>, response: Response<AllFeedResponse>) {
                if (response.isSuccessful) {
                    _foodData.postValue(response.body()?.data!!.toMutableList())
                }
            }

            override fun onFailure(call: Call<AllFeedResponse>, t: Throwable) {
                t
            }

        })
    }

    // FoodList를 LiveData 객채로 생성
    private val _foodData = MutableLiveData<List<FeedData>>().apply {
    }
    val foodData : LiveData<List<FeedData>> = _foodData
}