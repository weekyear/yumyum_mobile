package com.omnyom.yumyum.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omnyom.yumyum.RetrofitBuilder
import com.omnyom.yumyum.interfaces.RetrofitService
import com.omnyom.yumyum.model.login.LoginRequest
import com.omnyom.yumyum.model.login.LoginResponse
import com.omnyom.yumyum.model.signup.SignUpRequest
import com.omnyom.yumyum.model.signup.SignUpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private var retrofitService: RetrofitService = RetrofitBuilder.buildService(RetrofitService::class.java)

    // A placeholder username validation check
    fun isEmailValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    fun login(email: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        val call = retrofitService.login(email)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                response
                if (response.code() == 404) {
                    onFailure()
                } else if (response.code() in 200..299) {
                    onSuccess()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                onFailure()
            }
        })
    }
}