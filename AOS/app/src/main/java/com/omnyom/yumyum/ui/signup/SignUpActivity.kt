package com.omnyom.yumyum.ui.signup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.omnyom.yumyum.MainActivity
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.ActivitySignUpBinding
import com.omnyom.yumyum.ui.login.LoginViewModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignUpBinding
    private lateinit var loginViewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()
    }

    private fun startMainActivity() {
        val intent = Intent(application, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}