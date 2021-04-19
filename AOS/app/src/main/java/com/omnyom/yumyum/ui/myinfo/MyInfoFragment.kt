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
        myInfoViewModel.text.observe(viewLifecycleOwner, Observer {
            binding.textProfile.text = it
        })

        binding.logOutButton.setOnClickListener {
            signOut()
        }

        return root
    }

    private val googleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        GoogleSignIn.getClient(requireActivity().parent, gso)
    }

    private fun signOut() {
        firebaseAuth?.signOut()
        googleSignOut{startLoginActivity()}
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