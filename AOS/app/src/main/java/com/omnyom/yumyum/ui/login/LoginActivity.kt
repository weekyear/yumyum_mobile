package com.omnyom.yumyum.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.omnyom.yumyum.MainActivity
import com.omnyom.yumyum.databinding.ActivityLoginBinding
import com.omnyom.yumyum.helper.GoogleLoginHelper.Companion.RESULT_CODE
import com.omnyom.yumyum.helper.GoogleLoginHelper.Companion.firebaseAuth
import com.omnyom.yumyum.helper.GoogleLoginHelper.Companion.googleSignClient
import com.omnyom.yumyum.helper.GoogleLoginHelper.Companion.googleSignIn
import com.omnyom.yumyum.helper.GoogleLoginHelper.Companion.initGoogleSignInIntent
import com.omnyom.yumyum.ui.signup.SignUpActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding :ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        if (firebaseAuth!!.currentUser != null) {
            startMainActivity()
        }

        initGoogleSignInIntent(this)

        binding.signInButton.setOnClickListener {
            startActivityForResult(googleSignClient.signInIntent, RESULT_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == RESULT_CODE) {
            if (googleSignIn(data)) {
                // 백엔드랑 통신해서 유저 정보 있으면
//                startMainActivity()
                // 백엔드랑 통신해서 유저 정보 없으면
                startSignUpActivity()
            }
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(application, MainActivity::class.java))
        finish()
    }

    private fun startSignUpActivity() {
        startActivity(Intent(application, SignUpActivity::class.java))
        finish()
    }
}