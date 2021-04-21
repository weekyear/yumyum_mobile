package com.omnyom.yumyum.ui.feed

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.omnyom.yumyum.databinding.ActivityFeedCreateBinding

class FeedCreateActivity : AppCompatActivity() {
    val binding by lazy { ActivityFeedCreateBinding.inflate(layoutInflater) }
    private lateinit var feedCreateViewModel: FeedCreateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.videoTest.setVideoURI(Uri.parse(intent.getStringExtra("videoUri")))
        binding.videoTest.setOnPreparedListener {mp ->
            binding.videoTest.start()
            mp!!.isLooping = true
        }
        binding.textTest.text = intent.getStringExtra("videoUri")
        binding.btnGoBack.setOnClickListener { finish() }

        textWatcher()
    }

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
}



