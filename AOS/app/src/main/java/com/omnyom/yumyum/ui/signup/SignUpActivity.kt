package com.omnyom.yumyum.ui.signup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.omnyom.yumyum.MainActivity
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.ActivityLoginBinding
import com.omnyom.yumyum.databinding.ActivitySignUpBinding
import com.omnyom.yumyum.ui.login.LoginViewModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        super.onCreate(savedInstanceState)
        setContentView(view)
        supportActionBar?.hide()
        textWatcher()
    }

    fun startMainActivity(v: View) {
        val intent = Intent(application, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun textWatcher() {
        binding.editTextName.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

            override fun afterTextChanged(s: Editable?) {
                if (binding.editTextName.text!!.isEmpty()) {
                    binding.textInputLayoutName.error = getString(R.string.please_write_name)
                    setBtnCompleteEnabled(false)
                } else {
                    binding.textInputLayoutName.error = null
                    if (binding.editTextIntroduction.text!!.isNotEmpty()) {
                        setBtnCompleteEnabled(true)
                    }
                }
            }
        })

        binding.editTextIntroduction.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

            override fun afterTextChanged(s: Editable?) {
                if (binding.editTextIntroduction.text!!.isEmpty()) {
                    binding.textInputLayoutIntroduction.error = getString(R.string.please_write_introduction)
                    setBtnCompleteEnabled(false)
                } else {
                    binding.textInputLayoutIntroduction.error = null
                    if (binding.editTextName.text!!.isNotEmpty()) {
                        setBtnCompleteEnabled(true)
                    }
                }
            }
        })
    }

    private fun setBtnCompleteEnabled (isEnabled: Boolean) {
        if (isEnabled) {
            binding.btnComplete.isEnabled = true
            binding.btnComplete.background = ContextCompat.getDrawable(baseContext, R.drawable.enabled_btn)
        } else {
            binding.btnComplete.isEnabled = false
            binding.btnComplete.background = ContextCompat.getDrawable(baseContext, R.drawable.disabled_btn)
        }
    }
}