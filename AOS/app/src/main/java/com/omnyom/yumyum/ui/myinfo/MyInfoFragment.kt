package com.omnyom.yumyum.ui.myinfo

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.omnyom.yumyum.databinding.FragmentMyInfoBinding
import com.omnyom.yumyum.ui.useroption.MyOptionActivity


class MyInfoFragment : Fragment() {
    private var _binding: FragmentMyInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var myInfoViewModel: MyInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyInfoBinding.inflate(inflater, container, false)
        val root = binding.root
        // Inflate the layout for this fragment
        myInfoViewModel = ViewModelProvider(this).get(MyInfoViewModel::class.java)

        myInfoViewModel.userData.observe(viewLifecycleOwner, Observer {
            binding.tvUsername.text = it.nickname
            binding.tvIntroduction.text = it.introduction
            Glide.with(this).load(it.profilePath).override(120,120).circleCrop().into(binding.ivAvatar)
        })

        binding.btnGoOptions.setOnClickListener {
            goMyOption()
        }

        return root
    }

    private fun goMyOption() {
        val intent = Intent(context, MyOptionActivity::class.java)
        startActivity(intent)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}