package com.omnyom.yumyum.ui.search

import android.app.Activity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.FragmentPlaceListBinding
import com.omnyom.yumyum.helper.recycler.SearchKakaoPlaceAdapter
import com.omnyom.yumyum.helper.recycler.SearchPlaceAdapter
import com.omnyom.yumyum.ui.base.BaseBindingFragment

class PlaceListFragment : BaseBindingFragment<FragmentPlaceListBinding>(R.layout.fragment_place_list) {
    private val searchVM: SearchViewModel by activityViewModels()

    override fun extraSetupBinding() { }

    override fun setup() { }

    override fun setupViews() {
        binding.rvPlace.apply {
            adapter = SearchPlaceAdapter(activity as Activity).apply {
                setVM(searchVM)
            }
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onSubscribe() {
        searchVM.searchPlaceResults.observe(viewLifecycleOwner, {
            val adapter = binding.rvPlace.adapter as SearchPlaceAdapter
            adapter.run {
                setItems(it)
                notifyDataSetChanged()
            }
        })
    }

    override fun release() { }

}