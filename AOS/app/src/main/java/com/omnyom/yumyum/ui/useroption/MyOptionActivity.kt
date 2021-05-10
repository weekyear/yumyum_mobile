package com.omnyom.yumyum.ui.useroption

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.omnyom.yumyum.MainActivity
import com.omnyom.yumyum.databinding.ActivityMyOptionBinding
import com.omnyom.yumyum.helper.GoogleLoginHelper
import com.omnyom.yumyum.ui.login.LoginActivity

class MyOptionActivity : AppCompatActivity() {

    val binding by lazy { ActivityMyOptionBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnLogout.setOnClickListener { signOut() }
        binding.btnEditProfile.setOnClickListener { goUserEdit() }
        binding.btnInfoAgreement.setOnClickListener { goInfoAgreement() }


    }

    private fun goInfoAgreement() {
        binding.wvInfoAgreement.loadUrl("https://week-year.tistory.com/190?category=891710")
    }

    private fun signOut() {
        GoogleLoginHelper.firebaseAuth.signOut()
        GoogleLoginHelper.googleSignOut(this, { startLoginActivity() })
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    fun goUserEdit() {
        val intent = Intent(this, UserInfoEditActivity::class.java)
        startActivity(intent)
    }
}