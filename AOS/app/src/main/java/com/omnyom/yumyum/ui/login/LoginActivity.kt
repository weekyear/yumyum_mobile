package com.omnyom.yumyum.ui.login

import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.gms.auth.api.Auth
import com.omnyom.yumyum.MainActivity
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.ActivityLoginBinding
import com.omnyom.yumyum.helper.GoogleLoginHelper.Companion.RESULT_CODE
import com.omnyom.yumyum.helper.GoogleLoginHelper.Companion.firebaseAuth
import com.omnyom.yumyum.helper.GoogleLoginHelper.Companion.getGoogleSignInIntent
import com.omnyom.yumyum.helper.GoogleLoginHelper.Companion.googleSignIn
import com.omnyom.yumyum.helper.PreferencesManager
import com.omnyom.yumyum.ui.base.BaseBindingActivity
import com.omnyom.yumyum.ui.signup.SignUpActivity


class LoginActivity: BaseBindingActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private val loginVM: LoginViewModel by viewModels()
    private lateinit var sharedPref: SharedPreferences

    override fun extraSetupBinding() {
        binding.apply {
            vm = loginVM
            lifecycleOwner = this@LoginActivity
        }
    }

    override fun setup() {
        sharedPref = this?.getPreferences(MODE_PRIVATE) ?: return

        if (firebaseAuth.currentUser == null) {
            PreferencesManager.setString(this, getString(R.string.saved_google_email), "")
        } else {
            loginVM.login(firebaseAuth.currentUser.email, { startMainActivity() }, { Toast.makeText(this, "로그인이 불안정합니다.", Toast.LENGTH_LONG).show()})
        }
    }

    override fun setupViews() {
        supportActionBar?.hide()
    }

    override fun onSubscribe() {
        binding.signInButton.setOnClickListener {
            startActivityForResult(getGoogleSignInIntent(this).signInIntent, RESULT_CODE)
        }
    }

    override fun release() { }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
        Log.e("RESULT", result?.status.toString())

        if (resultCode == RESULT_OK && requestCode == RESULT_CODE) {
            val loginEmail = googleSignIn(data)
            if (loginVM.isEmailValid(loginEmail)) {
                // SharedPreferences에 이메일 저장
                PreferencesManager.setString(this, getString(R.string.saved_google_email), loginEmail)
                loginVM.login(loginEmail, { startMainActivity() }, { startSignUpActivity() })
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