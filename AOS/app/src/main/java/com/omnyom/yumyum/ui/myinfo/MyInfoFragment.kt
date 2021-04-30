package com.omnyom.yumyum.ui.myinfo

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.FragmentMyInfoBinding
import com.omnyom.yumyum.ui.login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.omnyom.yumyum.MainActivity
import com.omnyom.yumyum.helper.GoogleLoginHelper.Companion.firebaseAuth
import com.omnyom.yumyum.helper.GoogleLoginHelper.Companion.googleSignOut


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
        })

        binding.logOutButton.setOnClickListener {
            signOut()
        }

        return root
    }

    private fun signOut() {
        firebaseAuth.signOut()
        googleSignOut(activity as MainActivity, {startLoginActivity()})
    }

    private fun startLoginActivity() {
        val intent = Intent(activity?.application, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}