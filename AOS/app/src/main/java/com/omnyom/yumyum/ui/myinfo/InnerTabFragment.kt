package com.omnyom.yumyum.ui.myinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

import com.omnyom.yumyum.databinding.FragmentInnerTabBinding
import com.omnyom.yumyum.model.myinfo.PagerAdapters

class InnerTabFragment : Fragment() {

    val binding by lazy { FragmentInnerTabBinding.inflate(layoutInflater) }
    lateinit var tabs: TabLayout
    lateinit var title: TextView
    lateinit var viewPager: ViewPager
    lateinit var pagerAdapters: PagerAdapters

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        tabs = binding.tabs
        viewPager = binding.viewPager
        pagerAdapters = PagerAdapters(childFragmentManager)

        pagerAdapters.addFragment(MyFeedFragment(), "MY Feed")
        pagerAdapters.addFragment(LikeFeedFragment(), "Liked Feed")

        viewPager.adapter = pagerAdapters

        tabs.setupWithViewPager(viewPager)

        return binding.root
    }


}