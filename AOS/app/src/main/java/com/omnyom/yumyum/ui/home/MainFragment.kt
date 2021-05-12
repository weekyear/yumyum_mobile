package com.omnyom.yumyum.ui.home

import androidx.fragment.app.*
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.FragmentMainBinding
import com.omnyom.yumyum.helper.recycler.FlipFeedAdapter
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.ui.base.BaseBindingFragment
import com.omnyom.yumyum.ui.base.BaseViewModel
import com.omnyom.yumyum.ui.search.SearchFragment

class MainFragment : BaseBindingFragment<FragmentMainBinding>(R.layout.fragment_main) {
    private val mainVM: MainViewModel by viewModels()

    override fun extraSetupBinding() { }

    override fun setup() {  }

    override fun setupViews() {
        val viewPager = binding.viewPager
        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
        val pagerAdapters = FlipFeedPagerAdapter(requireActivity())
        viewPager.adapter = pagerAdapters
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

    class FlipFeedPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
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
                tempList.add(FeedFragment(feed))
            }
            items.addAll(tempList)
        }

        fun addItems(feedList: List<FeedData>) {
            val tempList = mutableListOf<Fragment>()
            for (feed in feedList) {
                tempList.add(FeedFragment(feed))
            }
            items.addAll(tempList)
        }

        fun addItem(feed: FeedData) {
            items.add(FeedFragment(feed))
        }
    }
}