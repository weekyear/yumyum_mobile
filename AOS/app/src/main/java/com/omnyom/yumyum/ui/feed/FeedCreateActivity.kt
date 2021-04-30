package com.omnyom.yumyum.ui.feed

import android.content.Intent
import android.net.Uri
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
        binding.btnSubmit.setOnClickListener {
            feedCreateVM.sendVideo(getMultipartBodyOfVideo(intent.getStringExtra("videoUri")!!.toUri()))
        }

        binding.btnMap.setOnClickListener { startMapsActivity() }

        textWatcher()
    }

    override fun onSubscribe() {
    }

    override fun release() {
    }

    // 텍스트 변경을 감지합니다.
    private fun textWatcher() {
        binding.tvTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (binding.tvTitle.text!!.isEmpty() || binding.tvTitle.text!!.length > 10) {
                    binding.inputLayoutTitle.error = "올바른 입력값이 아닙니다!"
                } else {
                    binding.inputLayoutTitle.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }


    // 지도 Activity로 이동해요
    private fun startMapsActivity() {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

    private fun getMultipartBodyOfVideo(videoUri: Uri?): MultipartBody.Part? {
        val parcelFileDescriptor = contentResolver.openFileDescriptor(videoUri!!, "r", null) ?: return null
        val file = File(cacheDir, contentResolver.getFileName(videoUri!!))
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        val requestFile = RequestBody.create("video/mp4".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    }
}



