package com.omnyom.yumyum.ui.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omnyom.yumyum.model.maps.KeywordSearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class LocationListViewModel: ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "음식 이름을 적어주세요"
    }
    val text: LiveData<String> = _text

//    fun Search() {
//        var call = RetrofitManager.kakaoApiService.placeSearch(kakaoApi.API_KEY, inputPlaceName.text.toString(), x.toDouble(), y.toDouble(), 1, 5)
//        call.enqueue(object : Callback<KeywordSearchResponse> {
//            override fun onResponse(call: Call<KeywordSearchResponse>, response: Response<KeywordSearchResponse>) {
//                if (response.isSuccessful) {
//                }
//            }
//
//            override fun onFailure(call: Call<KeywordSearchResponse>, t: Throwable) {
//                t
//                TODO("Not yet implemented")
//            }
//
//        })
//    }
}