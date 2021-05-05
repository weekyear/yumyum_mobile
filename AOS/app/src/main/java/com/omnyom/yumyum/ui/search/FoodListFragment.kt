package com.omnyom.yumyum.ui.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.FragmentFoodListBinding
import com.omnyom.yumyum.databinding.FragmentSearchInnerTabBinding
import com.omnyom.yumyum.ui.base.BaseBindingFragment

class FoodListFragment : BaseBindingFragment<FragmentFoodListBinding>(R.layout.fragment_food_list) {
    private val searchVM: SearchViewModel by activityViewModels()

    override fun extraSetupBinding() { }

    override fun setup() { }

    override fun setupViews() { }

    override fun onSubscribe() { }

    override fun release() { }
}