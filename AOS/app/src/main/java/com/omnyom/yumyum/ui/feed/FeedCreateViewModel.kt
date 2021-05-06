package com.omnyom.yumyum.ui.feed

import android.app.Application
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omnyom.yumyum.RetrofitBuilder
import com.omnyom.yumyum.helper.PreferencesManager
import com.omnyom.yumyum.helper.RetrofitManager.Companion.retrofitService
import com.omnyom.yumyum.helper.getFileName
import com.omnyom.yumyum.interfaces.RetrofitService
import com.omnyom.yumyum.model.feed.CreateFeedRequest
import com.omnyom.yumyum.model.feed.CreateFeedResponse
import com.omnyom.yumyum.model.feed.PlaceRequest
import com.omnyom.yumyum.model.feed.SendVideoResponse
import com.omnyom.yumyum.ui.base.BaseViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class FeedCreateViewModel(application: Application) : BaseViewModel(application) {

    private var videoPath: String = ""
    private var thumbnailPath: String = ""

    var placeRequest: PlaceRequest = PlaceRequest("", 0.0, 0.0, "", "")

    val content = MutableLiveData<String>().apply {
        value = ""
    }
    val score = MutableLiveData<Int>().apply {
        value = 0
    }
    val title = MutableLiveData<String>().apply {
        value = ""
    }

    // 비디오 데이터를 보냅니다!
    fun sendVideo(body: MultipartBody.Part?) {
        retrofitService.sendVideo(body!!).enqueue(object : Callback<SendVideoResponse> {
            override fun onResponse(call: Call<SendVideoResponse>, response: Response<SendVideoResponse>) {
                if (response.isSuccessful) {
                    Log.d("createFeed", "${response.body()}")
                    videoPath = response.body()?.data!!.videoPath
                    thumbnailPath = response.body()?.data!!.thumbnailPath
                    createFeed()
                }
            }

            override fun onFailure(call: Call<SendVideoResponse>, t: Throwable) {
                t
            }

        })
    }

    // 응답으로 받은 비디오 데이터를 넣어서 피드 전체 데이터를 보냅니다!
    fun createFeed() {
        val userId = PreferencesManager.getLong(getApplication(), "userId")

        val createFeedRequest = CreateFeedRequest(content.value?:"", placeRequest, score.value?:0, thumbnailPath, title.value?:"", userId?:0, videoPath)

        retrofitService.createFeed(createFeedRequest.get()).enqueue(object : Callback<CreateFeedResponse> {
            override fun onResponse(call: Call<CreateFeedResponse>, response: Response<CreateFeedResponse>) {
                if (response.isSuccessful) {
                    Log.d("createFeed", "${response.body()}")
                }
            }

            override fun onFailure(call: Call<CreateFeedResponse>, t: Throwable) {
                Log.d("createFeed", "${t}")
                t
            }

        })
    }
}