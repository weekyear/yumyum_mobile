package com.omnyom.yumyum.ui.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.FragmentSearchBinding
import com.omnyom.yumyum.databinding.FragmentSearchInnerTabBinding
import com.omnyom.yumyum.model.myinfo.PagerAdapters
import com.omnyom.yumyum.ui.base.BaseBindingFragment

class SearchInnerTabFragment : BaseBindingFragment<FragmentSearchInnerTabBinding>(R.layout.fragment_search_inner_tab) {
    private val searchVM: SearchViewModel by viewModels({requireParentFragment()})

    override fun extraSetupBinding() { }

    override fun setup() { }

    override fun setupViews() {
        val tabs = binding.tabsSearch
        val viewPager = binding.vpSearch
        val pagerAdapters = PagerAdapters(childFragmentManager)

        pagerAdapters.addFragment(FoodListFragment(), "음식별")
        pagerAdapters.addFragment(PlaceListFragment(), "식당별")

        viewPager.adapter = pagerAdapters

        tabs.setupWithViewPager(viewPager)
    }

    override fun onSubscribe() {
        searchVM.searchPlaceResults.observe(viewLifecycleOwner, {
            it
        })
    }

    override fun release() { }

}