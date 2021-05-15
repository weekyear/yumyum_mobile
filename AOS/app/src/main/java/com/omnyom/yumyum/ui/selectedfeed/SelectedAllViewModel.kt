package com.omnyom.yumyum.ui.selectedfeed

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.ui.base.BaseViewModel

class SelectedAllViewModel(application: Application) : BaseViewModel(application) {
    private val _feedData = MutableLiveData<List<FeedData>>().apply {
    }
    val foodData : LiveData<List<FeedData>> = _feedData

    lateinit var position : String

    fun getData(intent: Intent) {
        val comeData = intent.getSerializableExtra("feedData") as ArrayList<FeedData>
        position = intent.getIntExtra("position", 0).toString()
        comeData.toList()
        Log.d("comeData", "${comeData}")
        _feedData.postValue(comeData)

    }


}
