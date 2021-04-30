package com.omnyom.yumyum.ui.myinfo

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omnyom.yumyum.TempRetrofitBuilder
import com.omnyom.yumyum.helper.PreferencesManager
import com.omnyom.yumyum.interfaces.RetrofitService
import com.omnyom.yumyum.model.userInfo.UserData
import com.omnyom.yumyum.model.userInfo.UserResponse
import com.omnyom.yumyum.ui.base.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyInfoViewModel(application: Application) : BaseViewModel(application) {
    private var myRetrofitService: RetrofitService = TempRetrofitBuilder.buildService(RetrofitService::class.java)


    init {
        val userId = PreferencesManager.getLong(getApplication(), "userId")
        var myFeedCall = myRetrofitService.getUserData(userId!!)
        myFeedCall.enqueue(object : Callback<UserResponse> {
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

    private val _userData = MutableLiveData<UserData>().apply {

    }
    val userData : LiveData<UserData> = _userData
}