package com.omnyom.yumyum.ui.myinfo

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omnyom.yumyum.TempRetrofitBuilder
import com.omnyom.yumyum.helper.PreferencesManager
import com.omnyom.yumyum.helper.PreferencesManager.Companion.userId
import com.omnyom.yumyum.helper.RetrofitManager.Companion.retrofitService
import com.omnyom.yumyum.interfaces.RetrofitService
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.model.feed.FeedResponse
import com.omnyom.yumyum.model.feed.Place
import com.omnyom.yumyum.model.userInfo.UserData
import com.omnyom.yumyum.model.userInfo.UserResponse
import com.omnyom.yumyum.ui.base.BaseViewModel
import com.omnyom.yumyum.ui.markermap.MarkerMapActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyInfoViewModel(application: Application) : BaseViewModel(application) {

    init {
        getUserData()
    }

    private val _userData = MutableLiveData<UserData>().apply { }
    val userData : LiveData<UserData> = _userData

    fun getUserData() {
        retrofitService.getUserData(userId).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    _userData.postValue(response.body()?.data!!)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                t
            }

        })
    }
}