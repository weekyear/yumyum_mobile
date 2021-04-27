package com.omnyom.yumyum.ui.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FeedCreateViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "음식 이름을 적어주세요"
    }
    val text: LiveData<String> = _text
}