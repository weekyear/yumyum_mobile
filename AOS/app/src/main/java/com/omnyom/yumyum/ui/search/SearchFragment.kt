package com.omnyom.yumyum.ui.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
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
import com.omnyom.yumyum.ui.home.HomeFragment

class SearchFragment : BaseBindingFragment<FragmentSearchBinding>(R.layout.fragment_search) {
    private val searchVM: SearchViewModel by viewModels()
    private lateinit var callback: OnBackPressedCallback

    override fun extraSetupBinding() { }

    override fun setup() {
    }

    override fun setupViews() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchVM.searchFeed(query!!)
                searchVM.searchPlace(query!!)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    override fun onSubscribe() { }

    override fun release() { }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(R.id.nav_host_fragment, HomeFragment())
                transaction.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

}