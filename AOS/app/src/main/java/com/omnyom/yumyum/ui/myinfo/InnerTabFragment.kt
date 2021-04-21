package com.omnyom.yumyum.ui.myinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator

import com.omnyom.yumyum.databinding.FragmentInnerTabBinding

class InnerTabFragment : Fragment() {

    val binding by lazy { FragmentInnerTabBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return binding.root

    }


}