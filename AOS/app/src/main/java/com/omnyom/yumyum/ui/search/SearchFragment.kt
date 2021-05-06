package com.omnyom.yumyum.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.FragmentSearchBinding
import com.omnyom.yumyum.helper.recycler.SearchPlaceAdapter
import com.omnyom.yumyum.model.myinfo.PagerAdapters
import com.omnyom.yumyum.ui.base.BaseBindingFragment
import com.omnyom.yumyum.ui.feed.SearchPlaceViewModel

class SearchFragment : BaseBindingFragment<FragmentSearchBinding>(R.layout.fragment_search) {
    private val searchVM: SearchViewModel by viewModels()

    override fun extraSetupBinding() { }

    override fun setup() { }

    override fun setupViews() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchVM.searchPlace(query!!)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    override fun onSubscribe() {
        searchVM.searchPlaceResults.observe(viewLifecycleOwner, {
            it
//            val adapter = binding.rvPlace.adapter as SearchPlaceAdapter
//            adapter.run {
//                setItems(it)
//                notifyDataSetChanged()
//            }
        })
    }

    override fun release() { }
}