package com.omnyom.yumyum.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import androidx.activity.viewModels
import com.google.android.gms.auth.api.Auth
import com.omnyom.yumyum.MainActivity
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.ActivityLoginBinding
import com.omnyom.yumyum.helper.GoogleLoginHelper.Companion.RESULT_CODE
import com.omnyom.yumyum.helper.GoogleLoginHelper.Companion.firebaseAuth
import com.omnyom.yumyum.helper.GoogleLoginHelper.Companion.getGoogleSignInIntent
import com.omnyom.yumyum.helper.GoogleLoginHelper.Companion.googleSignClient
import com.omnyom.yumyum.helper.GoogleLoginHelper.Companion.googleSignIn
import com.omnyom.yumyum.ui.base.BaseBindingActivity
import com.omnyom.yumyum.ui.signup.SignUpActivity
import com.omnyom.yumyum.helper.PreferencesManager


class LoginActivity: BaseBindingActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private val loginVM: LoginViewModel by viewModels()
    private lateinit var sharedPref: SharedPreferences

    override fun extraSetupBinding() {
        binding.vm = loginVM
        binding.lifecycleOwner = this
    }

    override fun setup() {
        sharedPref = this?.getPreferences(Context.MODE_PRIVATE) ?: return
//        initGoogleSignInIntent(this)

        if (firebaseAuth!!.currentUser == null) {
            PreferencesManager.setString(this, getString(R.string.saved_google_email), "")
        } else {
            startMainActivity()
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

        if (resultCode == Activity.RESULT_OK && requestCode == RESULT_CODE) {
            val loginEmail = googleSignIn(data)
            if (loginVM.isEmailValid(loginEmail)) {
                // SharedPreferences에 이메일이 저장
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