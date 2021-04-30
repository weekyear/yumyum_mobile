package com.omnyom.yumyum.ui.login

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.util.Base64
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
import com.omnyom.yumyum.ui.feed.LocationListActivity
import com.omnyom.yumyum.ui.maps.MapsActivity
import com.omnyom.yumyum.ui.signup.SignUpActivity
import java.security.MessageDigest


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
            loginVM.login(firebaseAuth.currentUser.email, { startSearchActivity() }, { Toast.makeText(this, "로그인이 불안정합니다.", Toast.LENGTH_LONG).show()})
        }

        try {
            val packageInfo = packageManager.getPackageInfo(
                    packageName, PackageManager.GET_SIGNING_CERTIFICATES
            )
            val signingInfo = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                packageInfo.signingInfo.apkContentsSigners
            } else {
                TODO("VERSION.SDK_INT < P")
            }

            for (signature in signingInfo) {
                val messageDigest = MessageDigest.getInstance("SHA")
                messageDigest.update(signature.toByteArray())
                val keyHash = String(Base64.encode(messageDigest.digest(), 0))
                Log.d("KeyHash", keyHash)
            }

        } catch (e: Exception) {
            Log.e("Exception", e.toString())
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

        Auth.GoogleSignInApi.getSignInResultFromIntent(data)

        if (resultCode == RESULT_OK && requestCode == RESULT_CODE) {
            val loginEmail = googleSignIn(data)
            if (loginVM.isEmailValid(loginEmail)) {
                // SharedPreferences에 이메일 저장
                PreferencesManager.setString(this, getString(R.string.saved_google_email), loginEmail)
                loginVM.login(loginEmail, { startSearchActivity() }, { startSignUpActivity() })
            }
        }
    }

    private fun startMainActivity() {
        Intent(this, MainActivity::class.java).let {
            startActivity(it)
            finish()
        }
    }

    private fun startSignUpActivity() {
        Intent(this, SignUpActivity::class.java).let {
            startActivity(it)
            finish()
        }
    }

    // 지도 Activity로 이동해요
    private fun startMapsActivity() {
        Intent(this, MapsActivity::class.java).let {
            startActivity(it)
            finish()
        }
    }

    private fun startSearchActivity() {
        Intent(this, LocationListActivity::class.java).let {
            startActivity(it)
            finish()
        }
    }
}