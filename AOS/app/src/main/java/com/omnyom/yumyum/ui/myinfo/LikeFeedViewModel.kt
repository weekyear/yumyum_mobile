package com.omnyom.yumyum.ui.myinfo

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omnyom.yumyum.TempRetrofitBuilder
import com.omnyom.yumyum.helper.PreferencesManager
import com.omnyom.yumyum.interfaces.RetrofitService
import com.omnyom.yumyum.model.feed.AllFeedResponse
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.ui.base.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LikeFeedViewModel(application: Application) : BaseViewModel(application) {
    private var myRetrofitService: RetrofitService = TempRetrofitBuilder.buildService(RetrofitService::class.java)

    init {
        val userId = PreferencesManager.getLong(getApplication(), "userId")

        // 내 피드 불러오기
        var call = myRetrofitService.getLikedFeed(userId!!)
        call.enqueue(object : Callback<AllFeedResponse> {
            override fun onResponse(call: Call<AllFeedResponse>, response: Response<AllFeedResponse>) {
                if (response.isSuccessful) {
                    _likeFeedData.postValue(response.body()?.data!!.toMutableList())
                }
            }

            override fun onFailure(call: Call<AllFeedResponse>, t: Throwable) {
                t
            }

        })

    }

    // FoodList를 LiveData 객채로 생성
    private val _likeFeedData = MutableLiveData<List<FeedData>>().apply {
    }
    val likeFeedData : LiveData<List<FeedData>> = _likeFeedData

}