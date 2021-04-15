package com.omnyom.yumyum.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.FragmentHomeBinding
import com.omnyom.yumyum.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root = binding.root
        // Inflate the layout for this fragment
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        profileViewModel.text.observe(viewLifecycleOwner, Observer {
            binding.textProfile.text = it
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}