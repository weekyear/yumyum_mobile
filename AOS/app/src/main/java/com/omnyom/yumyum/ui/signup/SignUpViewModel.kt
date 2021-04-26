package com.omnyom.yumyum.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omnyom.yumyum.RetrofitBuilder
import com.omnyom.yumyum.interfaces.RetrofitService
import com.omnyom.yumyum.model.signup.SignUpRequest
import com.omnyom.yumyum.model.signup.SignUpResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.*

class SignUpViewModel : ViewModel() {
    private var retrofitService: RetrofitService = RetrofitBuilder.buildService(RetrofitService::class.java)

    private val _name = MutableLiveData<String>().apply {
        value = ""
    }
    val name: LiveData<String> = _name
    private val _introduction = MutableLiveData<String>().apply {
        value = ""
    }
    val introduction: LiveData<String> = _introduction

    private val _complete: MutableLiveData<Boolean> = MutableLiveData()
    val complete: LiveData<Boolean>
        get() = _complete

    fun completeSignUp() {
        _complete.value = true
    }

    fun signUp(email: String) {
        val call = retrofitService.signup(SignUpRequest(email,
                name.value?:"",
                introduction.value?:"").get())
        call.enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                TODO("Not yet implemented")
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}