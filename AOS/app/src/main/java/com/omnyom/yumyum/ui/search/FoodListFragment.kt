package com.omnyom.yumyum.ui.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.FragmentFoodListBinding

class FoodListFragment : Fragment() {
    val binding by lazy { FragmentFoodListBinding.inflate(layoutInflater)}
    private lateinit var viewModel: FoodListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }


}