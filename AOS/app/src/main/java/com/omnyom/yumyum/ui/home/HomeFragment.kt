package com.omnyom.yumyum.ui.home

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.FragmentHomeBinding
import com.omnyom.yumyum.helper.recycler.FlipFeedAdapter
import com.omnyom.yumyum.ui.base.BaseBindingFragment
import com.omnyom.yumyum.ui.search.SearchFragment

class HomeFragment : BaseBindingFragment<FragmentHomeBinding>(R.layout.fragment_home)  {
    private val homeVM: HomeViewModel by viewModels()

    override fun extraSetupBinding() { }

    override fun setup() {  }

    override fun setupViews() {
        binding.viewPagerHome.orientation = ViewPager2.ORIENTATION_VERTICAL

        binding.viewPagerHome.apply {
            adapter = FlipFeedAdapter(requireContext()).apply {
                setVM(homeVM)
            }
        }

        binding.icSearch.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, SearchFragment())
            transaction.commit()
        }
    }

    override fun onSubscribe() {
        homeVM.foodData.observe(viewLifecycleOwner, Observer {
            val adapter = binding.viewPagerHome.adapter as FlipFeedAdapter
            adapter.run {
                setItems(it)
                notifyDataSetChanged()
            }
        })
    }

    override fun release() { }
}