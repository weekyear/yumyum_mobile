package com.omnyom.yumyum.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.omnyom.yumyum.MainActivity
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding :ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private var firebaseAuth: FirebaseAuth? = null

    companion object {
        const val RESULT_CODE = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth!!.currentUser != null) {
            startMainActivity()
        }

        binding.signInButton.setOnClickListener {
            startActivityForResult(googleSignInIntent, RESULT_CODE)
        }
    }

    private val googleSignInIntent by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        GoogleSignIn.getClient(this, gso).signInIntent
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == RESULT_CODE) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)

            result?.let {
                if (it.isSuccess) {
                    if (it!!.isSuccess) {
                        firebaseLogin(result.signInAccount!!)
                    }

                    it.signInAccount?.displayName //이름
                    it.signInAccount?.email //이메일
//                    Log.e("Value", it.signInAccount?.email!!)

                    // 기타 등등
                } else  {
                    Log.e("Value", "error")
                    // 에러 처리
                }
            }
        }
    }

    private fun firebaseLogin(googleAccount: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(googleAccount.idToken, null)

        firebaseAuth?.signInWithCredential(credential)?.addOnCompleteListener {
            if (it.isSuccessful) {
                it.result?.user?.displayName //사용자 이름
                startMainActivity()
            } else {
                //error 처리
            }
        }?.addOnFailureListener {
            //error 처리
        }
    }

    private fun startMainActivity() {
        val intent = Intent(application, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}