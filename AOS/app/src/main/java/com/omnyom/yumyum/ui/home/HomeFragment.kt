package com.omnyom.yumyum.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.view.ContextThemeWrapper
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.FragmentHomeBinding
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.ui.base.BaseBindingFragment
import com.omnyom.yumyum.ui.feed.FeedCreateActivity
import com.omnyom.yumyum.ui.home.FeedFragment.Companion.curFeed

class HomeFragment : BaseBindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val homeVM: HomeViewModel by viewModels()

    override fun extraSetupBinding() { }

    override fun setup() {  }

    override fun setupViews() {
        binding.viewPager.run {
            orientation = ViewPager2.ORIENTATION_VERTICAL
            adapter = FlipFeedPagerAdapter(requireActivity())
        }

        binding.btnPopup.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)
            popupMenu.inflate(R.menu.feed_popup)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.edit_feed -> goEditFeed()
                    R.id.delete_feed -> deleteAlert()
                }
                false
            }
            popupMenu.show()
        }
    }

    override fun onSubscribe() {
        homeVM.foodData.observe(viewLifecycleOwner, {
            val adapter = binding.viewPager.adapter as FlipFeedPagerAdapter
            adapter.run {
                setItems(it)
                notifyDataSetChanged()
            }
        })
    }

    override fun release() { }

    // 피드 수정
    fun goEditFeed() {
        Intent(context, FeedCreateActivity::class.java).run {
            putExtra("FeedData", curFeed)
            context?.startActivity(this)
        }
    }

    fun deleteAlert() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.Theme_AppCompat_Light_Dialog))
        builder.setTitle("피드 삭제")
        builder.setMessage("정말 삭제하시겠습니까?")

        builder.setPositiveButton("삭제") { _, _ ->
            homeVM.deleteFeed(curFeed.id.toLong())
        }
        builder.setNegativeButton("취소") { _, _ ->
        }
        builder.show()
    }



    inner class FlipFeedPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
        private val items: MutableList<Fragment> = mutableListOf()

        override fun getItemCount(): Int {
            return items.size
        }

        override fun createFragment(position: Int): Fragment {
            return items[position]
        }

        fun setItems(feedList: List<FeedData>) {
            this.items.clear()
            val tempList = mutableListOf<Fragment>()
            for (feed in feedList) {
                tempList.add(FeedFragment(feed, homeVM))
            }
            items.addAll(tempList)
        }

        fun addItems(feedList: List<FeedData>) {
            val tempList = mutableListOf<Fragment>()
            for (feed in feedList) {
                tempList.add(FeedFragment(feed, homeVM))
            }
            items.addAll(tempList)
        }

        fun addItem(feed: FeedData) {
            items.add(FeedFragment(feed, homeVM))
        }
    }
}