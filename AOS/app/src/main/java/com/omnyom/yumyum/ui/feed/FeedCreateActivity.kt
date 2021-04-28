package com.omnyom.yumyum.ui.feed

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.viewModels
import androidx.core.net.toUri
import com.omnyom.yumyum.R
import com.omnyom.yumyum.TempRetrofitBuilder
import com.omnyom.yumyum.databinding.ActivityFeedCreateBinding
import com.omnyom.yumyum.helper.getFileName
import com.omnyom.yumyum.interfaces.RetrofitService
import com.omnyom.yumyum.model.feed.CreateFeedRequest
import com.omnyom.yumyum.model.feed.CreateFeedResponse
import com.omnyom.yumyum.model.feed.PlaceRequest
import com.omnyom.yumyum.model.feed.SendVideoResponse
import com.omnyom.yumyum.ui.base.BaseBindingActivity
import com.omnyom.yumyum.ui.maps.MapsActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class FeedCreateActivity : BaseBindingActivity<ActivityFeedCreateBinding>(R.layout.activity_feed_create) {
    var myRetrofitService: RetrofitService = TempRetrofitBuilder.buildService(RetrofitService::class.java)
    lateinit var videoPath: String
    lateinit var thumbnailPath: String

    private val feedCreateVM: FeedCreateViewModel by viewModels()


    override fun extraSetupBinding() {
        binding.apply {
            vm = feedCreateVM
            lifecycleOwner = this@FeedCreateActivity
        }
    }

    override fun setup() {
    }

    override fun setupViews() {
        binding.btnGoBack.setOnClickListener { finish() }
        binding.btnSubmit.setOnClickListener { sendVideo(intent.getStringExtra("videoUri")!!) }

        binding.btnMap.setOnClickListener { goMaps() }

        textWatcher()
    }

    override fun onSubscribe() {
    }

    override fun release() {
    }

    // 텍스트 변경을 감지합니다.
    private fun textWatcher() {
        binding.inputTextName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (binding.inputTextName.text!!.isEmpty() || binding.inputTextName.text!!.length > 10) {
                    binding.inputLayoutName.error = "올바른 입력값이 아닙니다!"
                } else {
                    binding.inputLayoutName.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }


    // 지도 Activity로 이동해요
    fun goMaps() {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

    // 비디오 데이터를 보냅니다!
    fun sendVideo(uri: String) {

        val parcelFileDescriptor = contentResolver.openFileDescriptor(uri.toUri(), "r", null) ?: return
        val file = File(cacheDir, contentResolver.getFileName(uri.toUri()))
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)


        val requestFile = RequestBody.create("video/mp4".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)


        var call = myRetrofitService.sendVideo(body)
        call.enqueue(object : Callback<SendVideoResponse> {
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
        val content = "아아! 마이크테스트"
        var placeRequest: PlaceRequest = PlaceRequest(address = "대전 덕명동", locationX = 36.35714736405305, locationY = 127.34172795397022, name = "염햄네 매운갈비찜", phone = "042-1234-1234")
        val score = 3
        val title = "퇴근"
        val userId = 10

        var call = myRetrofitService.createFeed(CreateFeedRequest(content, placeRequest, score, thumbnailPath, title, userId, videoPath  ).get())
        call.enqueue(object : Callback<CreateFeedResponse> {

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



