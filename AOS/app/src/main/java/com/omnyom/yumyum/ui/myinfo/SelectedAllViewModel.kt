package com.omnyom.yumyum.ui.myinfo

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.ui.base.BaseViewModel

class SelectedAllViewModel(application: Application) : BaseViewModel(application) {
    private val _feedData = MutableLiveData<List<FeedData>>().apply {
    }
    val feedData : LiveData<List<FeedData>> = _feedData

    var position : Int = 0

    fun getData(intent: Intent) {
        val comeData = intent.getSerializableExtra("feedData") as ArrayList<FeedData>
        position = intent.getIntExtra("position", 0)
        _feedData.postValue(comeData)
    }
}
