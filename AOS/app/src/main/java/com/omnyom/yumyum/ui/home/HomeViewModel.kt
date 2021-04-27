package com.omnyom.yumyum.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omnyom.yumyum.TempRetrofitBuilder
import com.omnyom.yumyum.interfaces.RetrofitService
import com.omnyom.yumyum.model.feed.AllFeedResponse
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.model.place.GetPlaceDataResponse

import retrofit2.*

class HomeViewModel : ViewModel() {
    private var myRetrofitService: RetrofitService = TempRetrofitBuilder.buildService(RetrofitService::class.java)

    init {
        var call = myRetrofitService.getAllFeeds(10)
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


    // 장소 불러오기
    fun getPlaceData() {
        var call = myRetrofitService.getPlaceData(3)
        call.enqueue(object : Callback<GetPlaceDataResponse> {
            override fun onResponse(call: Call<GetPlaceDataResponse>, response: Response<GetPlaceDataResponse>) {
                if (response.isSuccessful) {
                    Log.d("placeData", "오나?")
                }
            }

            override fun onFailure(call: Call<GetPlaceDataResponse>, t: Throwable) {
                t
            }

        })

    }






//    init {
//        var call = retrofitService.login(LoginRequest("jwnsgus@gmail.com", "wnsgus123").get())
//        call.enqueue(object : Callback<LoginResponse> {
//            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
//                if (response.isSuccessful) {
//                    response
//                    response.body()
//                }
//            }
//
//            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                t
//                TODO("Not yet implemented")
//            }
//
//        })
//    }

}

