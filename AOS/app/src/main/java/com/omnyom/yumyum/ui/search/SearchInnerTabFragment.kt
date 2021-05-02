package com.omnyom.yumyum.ui.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.FragmentSearchInnerTabBinding
import com.omnyom.yumyum.model.myinfo.PagerAdapters

class SearchInnerTabFragment : Fragment() {

    val binding by lazy { FragmentSearchInnerTabBinding.inflate(layoutInflater) }
    lateinit var tabs: TabLayout
    lateinit var viewPager: ViewPager
    lateinit var pagerAdapters: PagerAdapters
    private lateinit var viewModel: SearchInnerTabViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tabs = binding.tabsSearch
        viewPager = binding.vpSearch
        pagerAdapters = PagerAdapters(childFragmentManager)

        pagerAdapters.addFragment(FoodListFragment(), "음식별")
        pagerAdapters.addFragment(PlaceListFragment(), "식당별")

        viewPager.adapter = pagerAdapters

        tabs.setupWithViewPager(viewPager)

        return binding.root
    }

}