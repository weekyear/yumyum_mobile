package com.omnyom.yumyum

import com.google.gson.GsonBuilder
import com.omnyom.yumyum.helper.LoginJsonAdapter
import com.omnyom.yumyum.model.login.LoginResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private val client = OkHttpClient.Builder().build()

    val gson = GsonBuilder()
//            .registerTypeAdapter(LoginResponse::class.java, LoginJsonAdapter())
            .create()

    private val retrofit = Retrofit.Builder()
//            .baseUrl("http://10.0.2.2:8080/yumyum/")
            .baseUrl("http://k4b206.p.ssafy.io:8081/yumyum/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
}