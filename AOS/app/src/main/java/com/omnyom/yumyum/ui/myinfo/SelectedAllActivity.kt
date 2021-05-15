package com.omnyom.yumyum.ui.myinfo

import android.app.AlertDialog
import android.content.Intent
import android.view.ContextThemeWrapper
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.viewpager2.widget.ViewPager2
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.ActivitySelectedAllBinding
import com.omnyom.yumyum.helper.recycler.FeedFragment
import com.omnyom.yumyum.helper.recycler.FeedFragment.Companion.deleteAlert
import com.omnyom.yumyum.helper.recycler.FeedFragment.Companion.goEditFeed
import com.omnyom.yumyum.helper.recycler.FlipFeedAdapter
import com.omnyom.yumyum.helper.recycler.FlipFeedPagerAdapter
import com.omnyom.yumyum.ui.base.BaseBindingActivity
import com.omnyom.yumyum.ui.feed.FeedCreateActivity


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
            adapter = FlipFeedPagerAdapter(this@SelectedAllActivity)
            orientation = ViewPager2.ORIENTATION_VERTICAL
            post {
                binding.viewPagerSelectFeed.setCurrentItem(selectedAllVM.position,false)
            }
        }

        binding.btnPopup.setOnClickListener {
            val popupMenu = PopupMenu(this@SelectedAllActivity, it)
            popupMenu.inflate(R.menu.feed_popup)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.edit_feed -> goEditFeed(this)
                    R.id.delete_feed -> deleteAlert(this)
                }
                false
            }
            popupMenu.show()
        }

        supportActionBar?.hide()
    }

    override fun onSubscribe() {
        selectedAllVM.feedData.observe(this) {
            val adapter = binding.viewPagerSelectFeed.adapter as FlipFeedPagerAdapter
            adapter.run {
                setItems(it)
                notifyDataSetChanged()
            }
        }
    }

    override fun release() { }
}