package com.omnyom.yumyum.ui.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.FragmentPlaceListBinding

class PlaceListFragment : Fragment() {
    val binding by lazy { FragmentPlaceListBinding.inflate(layoutInflater) }
    private lateinit var viewModel: PlaceListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }
}