package com.omnyom.yumyum.ui.selectedfeed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.viewpager2.widget.ViewPager2
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.ActivitySelectedAllBinding
import com.omnyom.yumyum.helper.recycler.FlipFeedAdapter
import com.omnyom.yumyum.ui.base.BaseBindingActivity
import com.omnyom.yumyum.ui.userfeed.UserFeedViewModel

class SelectedAllActivity : BaseBindingActivity<ActivitySelectedAllBinding>(R.layout.activity_selected_all) {

    private val selectedAllVM: SelectedAllViewModel by viewModels()

    override fun extraSetupBinding() {
        binding.apply {
            vm = selectedAllVM
            lifecycleOwner = this@SelectedAllActivity
        }
    }

    override fun setup() {
        selectedAllVM.getData(intent)
    }

    override fun setupViews() {
        binding.viewPagerSelectFeed.apply {
            adapter = FlipFeedAdapter(context)
            orientation = ViewPager2.ORIENTATION_VERTICAL
            val position = selectedAllVM.position.toInt()
            post {
                binding.viewPagerSelectFeed.setCurrentItem(position ,false)
            }
        }
        supportActionBar?.hide()
    }

    override fun onSubscribe() {
        selectedAllVM.foodData.observe(this) {
            val adapter = binding.viewPagerSelectFeed.adapter as FlipFeedAdapter
            adapter.run {
                setItems(it)
                notifyDataSetChanged()
            }
        }
    }

    override fun release() {
    }

}