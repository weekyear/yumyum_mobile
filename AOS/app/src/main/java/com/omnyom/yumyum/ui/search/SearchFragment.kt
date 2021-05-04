package com.omnyom.yumyum.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.FragmentSearchBinding
import com.omnyom.yumyum.ui.base.BaseBindingFragment

class SearchFragment : BaseBindingFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    override fun extraSetupBinding() { }

    override fun setup() { }

    override fun setupViews() {
        binding.inputLayoutName.setEndIconActivated(true)
    }

    override fun onSubscribe() { }

    override fun release() { }
}