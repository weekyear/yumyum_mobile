package com.omnyom.yumyum.ui.home

import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.widget.PopupMenu
import androidx.fragment.app.*
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.FragmentMainBinding
import com.omnyom.yumyum.helper.RetrofitManager
import com.omnyom.yumyum.helper.recycler.FlipFeedAdapter
import com.omnyom.yumyum.model.feed.CreateFeedResponse
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.ui.base.BaseBindingFragment
import com.omnyom.yumyum.ui.base.BaseViewModel
import com.omnyom.yumyum.ui.feed.FeedCreateActivity
import com.omnyom.yumyum.ui.home.FeedFragment.Companion.curFeed
import com.omnyom.yumyum.ui.search.SearchFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainFragment : BaseBindingFragment<FragmentMainBinding>(R.layout.fragment_main) {
    private val mainVM: MainViewModel by viewModels()

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
                    R.id.delete_feed -> mainVM.deleteFeed(curFeed.id.toLong())
                }
                false
            }
        }
    }

    override fun onSubscribe() {
        mainVM.foodData.observe(viewLifecycleOwner, {
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
                tempList.add(FeedFragment(feed, mainVM))
            }
            items.addAll(tempList)
        }

        fun addItems(feedList: List<FeedData>) {
            val tempList = mutableListOf<Fragment>()
            for (feed in feedList) {
                tempList.add(FeedFragment(feed, mainVM))
            }
            items.addAll(tempList)
        }

        fun addItem(feed: FeedData) {
            items.add(FeedFragment(feed, mainVM))
        }
    }
}