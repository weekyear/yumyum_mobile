package com.omnyom.yumyum.ui.home

import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omnyom.yumyum.R
import com.omnyom.yumyum.RetrofitBuilder
import com.omnyom.yumyum.interfaces.RetrofitService
import com.omnyom.yumyum.model.login.LoginRequest
import com.omnyom.yumyum.model.login.LoginResponse
import retrofit2.*

class HomeViewModel : ViewModel() {
    private var retrofitService: RetrofitService = RetrofitBuilder.buildService(RetrofitService::class.java)

    init {
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
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    // FoodList를 LiveData 객채로 생성
    private val _foodData = MutableLiveData<List<HomeFragment.MyVideo>>().apply {
        value = getFoodList()
    }
    val foodData : LiveData<List<HomeFragment.MyVideo>> = _foodData


    // FoodList 만드는 function
    private fun getFoodList(): List<HomeFragment.MyVideo> {
        var myVideos: MutableList<HomeFragment.MyVideo> = mutableListOf()

        for (no in 1..5) {
            val detail = "감자탕 ${no}"
            val uri: Uri = Uri.parse("android.resource://" + "com.omnyom.yumyum" + "/" + R.raw.food3)
            var myVideo = HomeFragment.MyVideo(no, uri, detail)
            myVideos.add(myVideo)
        }

        return myVideos
    }


}

