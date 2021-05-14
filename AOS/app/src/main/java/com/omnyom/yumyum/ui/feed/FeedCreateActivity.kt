package com.omnyom.yumyum.ui.feed

import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.net.toUri
import androidx.lifecycle.observe
import com.airbnb.lottie.LottieAnimationView
import com.omnyom.yumyum.MainActivity
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.ActivityFeedCreateBinding
import com.omnyom.yumyum.helper.changeLayersColor
import com.omnyom.yumyum.helper.getFileName
import com.omnyom.yumyum.model.feed.EditPlaceRequest
import com.omnyom.yumyum.model.feed.PlaceRequest
import com.omnyom.yumyum.model.maps.SearchPlaceResult
import com.omnyom.yumyum.ui.base.BaseBindingActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class FeedCreateActivity : BaseBindingActivity<ActivityFeedCreateBinding>(R.layout.activity_feed_create) {

    private val feedCreateVM: FeedCreateViewModel by viewModels()
    private val avStars: List<LottieAnimationView> by lazy {
        arrayListOf(
                binding.avStar1,
                binding.avStar2,
                binding.avStar3,
                binding.avStar4,
                binding.avStar5
        )
    }

    override fun extraSetupBinding() {
        binding.apply {
            vm = feedCreateVM
            lifecycleOwner = this@FeedCreateActivity
        }
    }

    override fun setup() {
        feedCreateVM.getEditData(intent)
    }

    override fun setupViews() {
        supportActionBar?.hide()
        if (feedCreateVM.isEdit) {
            binding.btnSubmit.setOnClickListener {
                feedCreateVM.editFeed(feedCreateVM.editData.value?.id.toString().toLong())
                finish()
            }

        } else {
            binding.btnSubmit.setOnClickListener {
                feedCreateVM.sendVideo(getMultipartBodyOfVideo(intent.getStringExtra("videoUri")!!.toUri()))
                startMainActivity()
            }
        }

        binding.btnGoBack.setOnClickListener { finish() }
        binding.editTextPlace.setOnClickListener { startSearchPlaceActivity() }

        for ((idx, avStar) in avStars.withIndex()) {
            avStar.setOnClickListener {
                avStar.playAnimation()
                feedCreateVM.score.value = idx + 1
                changeAvStarColor(avStar.id)
                changeAvStarSize(avStar.id)
            }
        }

        textWatcher()
    }

    override fun onSubscribe() {
        feedCreateVM.run {
            for (elem in listOf(score, title, content, placeRequest)) {
                elem.observe(this@FeedCreateActivity, {
                    feedCreateVM.isCompleted = checkAllFeedRequest()
                    if (feedCreateVM.isCompleted) {
                        binding.btnSubmit.text = getString(R.string.complete)
                    } else {
                        binding.btnSubmit.text = getString(R.string.pre_save)
                    }
                })
            }
        }
        if (feedCreateVM.isEdit) {
            feedCreateVM.editData.observe(this) {
                val feedData = feedCreateVM.editData.value!!
                val starIds = arrayOf(0, 2131230792, 2131230793, 2131230794, 2131230795, 2131230796)
                binding.tvTitle.text = "피드 수정"
                binding.editTextContent.setText(feedData.content)
                binding.editTextTitle.setText(feedData.title)
                feedCreateVM.score.value = 1
                changeAvStarColor(starIds[feedData.score])
                changeAvStarSize(starIds[feedData.score])
                if (feedData.place != null) {
                    binding.editTextPlace.setText(feedData.place.name)
                }
            }
        }
    }

    override fun release() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SearchPlaceActivity.PLACE_CODE) {
            if (resultCode == RESULT_OK) {
                val placeResult = data?.getSerializableExtra("placeResult") as SearchPlaceResult
                if (feedCreateVM.isEdit) {
                    EditPlaceRequest(placeResult.address_name, 0,  placeResult.x.toDouble(), placeResult.y.toDouble(), placeResult.place_name, placeResult.phone)?.let {
                        feedCreateVM.editPlaceRequest.postValue(it)
                    }
                } else {
                    PlaceRequest(placeResult.address_name, placeResult.x.toDouble(), placeResult.y.toDouble(), placeResult.place_name, placeResult.phone)?.let {
                        feedCreateVM.placeRequest.postValue(it)
                    }
                }

                binding.editTextPlace.setText(placeResult.place_name)

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "장소 선택이 취소되었습니다.", Toast.LENGTH_LONG).show()
            }
        }
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

    private fun startSearchPlaceActivity() {
        val intent = Intent(this, SearchPlaceActivity::class.java)
        startActivityForResult(intent, SearchPlaceActivity.PLACE_CODE)
    }

    private fun startMainActivity() {
        Intent(this, MainActivity::class.java).let {
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(it)
        }
    }

    private fun getMultipartBodyOfVideo(videoUri: Uri?): MultipartBody.Part? {
        val parcelFileDescriptor = contentResolver.openFileDescriptor(videoUri!!, "r", null) ?: return null
        val file = File(cacheDir, contentResolver.getFileName(videoUri!!))
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        val requestFile = file.asRequestBody("video/mp4".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    }

    private fun changeAvStarColor(avStarId: Int) {
        for (avStar in avStars) {
            if (avStar.id == avStarId) {
                avStar.changeLayersColor(R.color.colorPrimary)
            } else {
                avStar.changeLayersColor(R.color.black)
            }
        }
    }

    private fun changeAvStarSize(avStarId: Int) {
        for (avStar in avStars) {
            if (avStar.id == avStarId) {
                avStar.loop(true)
                avStar.animate()
                        .scaleX(1.5f)
                        .scaleY(1.5f)
                        .setDuration(200)
                        .withEndAction {  }
            } else {
                avStar.loop(false)
                avStar.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(200)
                        .withEndAction {  }
            }
        }
    }

    private fun checkAllFeedRequest(): Boolean {
        feedCreateVM.run {
            return !(title.value.isNullOrBlank() || content.value.isNullOrBlank() || score.value == 0 || placeRequest.value?.name.isNullOrBlank())
        }
    }
}



