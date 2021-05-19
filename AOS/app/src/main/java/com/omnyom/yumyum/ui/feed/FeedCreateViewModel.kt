package com.omnyom.yumyum.ui.feed

import android.app.Application
import android.content.Intent
import android.util.Log
import android.util.Log.d
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.internal.FallbackServiceBroker
import com.omnyom.yumyum.RetrofitBuilder
import com.omnyom.yumyum.helper.PreferencesManager
import com.omnyom.yumyum.helper.PreferencesManager.Companion.userId
import com.omnyom.yumyum.helper.RetrofitManager.Companion.aiService
import com.omnyom.yumyum.helper.RetrofitManager.Companion.retrofitService
import com.omnyom.yumyum.helper.getFileName
import com.omnyom.yumyum.interfaces.RetrofitService
import com.omnyom.yumyum.model.feed.*
import com.omnyom.yumyum.ui.base.BaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Multipart
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import kotlin.concurrent.thread

class FeedCreateViewModel(application: Application) : BaseViewModel(application) {
    private var videoPath: String = ""
    private var thumbnailPath: String = ""
    var isCompleted: Boolean = false
    var isEdit: Boolean = false
    var isCalculated: Boolean = false

    val recommendTitles = MutableLiveData<List<String>>().apply {
        value = arrayListOf("    ", "   ", "    ")
    }

    val placeRequest = MutableLiveData<PlaceRequest?>().apply {
        value = null
    }
    val content = MutableLiveData<String>().apply {
        value = ""
    }
    val id = MutableLiveData<Long>().apply {
        value = 0
    }
    val score = MutableLiveData<Int>().apply {
        value = 0
    }
    val title = MutableLiveData<String>().apply {
        value = ""
    }
    
    // 비디오 데이터를 보냅니다!
    fun feedAi(video: MultipartBody.Part?) {
        aiService.feedAi(video!!).enqueue(object : Callback<FeedAiResponse> {
            override fun onResponse(call: Call<FeedAiResponse>, response: Response<FeedAiResponse>) {
                isCalculated = true
                if (response.isSuccessful) {
                    recommendTitles.postValue(response.body()?.predictions)
                } else {
                    recommendTitles.postValue(arrayListOf("   ", "   ", "    "))
                }
            }

            override fun onFailure(call: Call<FeedAiResponse>, t: Throwable) {
                isCalculated = true
                recommendTitles.postValue(arrayListOf("   ", "   ", "    "))
            }

        })
    }

    // 비디오 데이터를 보냅니다!
    fun sendVideo(body: MultipartBody.Part?) {
        retrofitService.sendVideo(body!!).enqueue(object : Callback<SendVideoResponse> {
            override fun onResponse(call: Call<SendVideoResponse>, response: Response<SendVideoResponse>) {
                if (response.isSuccessful) {
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
        val createFeedRequest = CreateFeedRequest(content.value?:"", isCompleted, placeRequest.value, score.value?:0, thumbnailPath, title.value?:"", userId, videoPath)

        retrofitService.createFeed(createFeedRequest.get()).enqueue(object : Callback<CreateFeedResponse> {
            override fun onResponse(call: Call<CreateFeedResponse>, response: Response<CreateFeedResponse>) {
                if (response.isSuccessful) {
                }
            }
            override fun onFailure(call: Call<CreateFeedResponse>, t: Throwable) {
                t
            }

        })
    }

    fun editFeed() {
        val editFeedRequest = EditFeedRequest(content.value?:"", id.value?:-1, isCompleted, placeRequest.value, score.value?:0,  title.value?:"", )

        retrofitService.editFeed(editFeedRequest.get()).enqueue(object : Callback<CreateFeedResponse> {
            override fun onResponse(call: Call<CreateFeedResponse>, response: Response<CreateFeedResponse>) {
                response
            }
            override fun onFailure(call: Call<CreateFeedResponse>, t: Throwable) {
                t
            }
        })
    }
}