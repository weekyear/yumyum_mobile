package com.omnyom.yumyum.helper

import com.omnyom.yumyum.KakaoRetrofitBuilder
import com.omnyom.yumyum.interfaces.KakaoApiService

class RetrofitManager {
    companion object {
        val kakaoApiService: KakaoApiService = KakaoRetrofitBuilder.buildService(KakaoApiService::class.java)
    }
}