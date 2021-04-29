package com.omnyom.yumyum.ui.signup

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.omnyom.yumyum.MainActivity
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.ActivitySignUpBinding
import com.omnyom.yumyum.helper.GoogleLoginHelper.Companion.firebaseAuth
import com.omnyom.yumyum.helper.GoogleLoginHelper.Companion.getCurrentUserEmail
import com.omnyom.yumyum.helper.PreferencesManager
import com.omnyom.yumyum.helper.getFileName
import com.omnyom.yumyum.ui.base.BaseBindingActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.*
import kotlin.math.sign


class SignUpActivity : BaseBindingActivity<ActivitySignUpBinding>(R.layout.activity_sign_up) {
    private companion object {
        const val IMAGE_CODE = 10
    }

    private val signUpVM: SignUpViewModel by viewModels()
    private var body : MultipartBody.Part? = null

    override fun extraSetupBinding() {
        binding.apply {
            vm = signUpVM
            lifecycleOwner = this@SignUpActivity
        }
    }

    override fun setup() { }

    override fun setupViews() {
        textWatcher()
        binding.btnAddProfile.setOnClickListener { getImageFromGrallery() }
        supportActionBar?.hide()
    }

    override fun onSubscribe() {
        signUpVM.isComplete.observe(this, {
            signUpVM.uploadProfileImage(body, getCurrentUserEmail(this), { startMainActivity(binding.btnComplete) }, { Log.e("Result", "Failed") })
        })
    }

    override fun release() { }

    fun startMainActivity(v: View) {
        val intent = Intent(application, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun textWatcher() {
        binding.editTextName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                signUpVM.name
            }

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

        binding.editTextIntroduction.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

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

    private fun getImageFromGrallery() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(intent, IMAGE_CODE)
    }

    private fun setBtnCompleteEnabled(isEnabled: Boolean) {
        if (isEnabled) {
            binding.btnComplete.isEnabled = true
            binding.btnComplete.background = ContextCompat.getDrawable(baseContext, R.drawable.btn_enabled)
        } else {
            binding.btnComplete.isEnabled = false
            binding.btnComplete.background = ContextCompat.getDrawable(baseContext, R.drawable.btn_disabled)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_CODE) {
            if (resultCode == RESULT_OK) {
                val imageUri = data?.data
                binding.btnAddProfile.setImageURI(imageUri)

                val bitmap = getBitmap(imageUri)
                val parcelFileDescriptor = contentResolver.openFileDescriptor(imageUri!!, "r", null) ?: return
                val file = File(cacheDir, contentResolver.getFileName(imageUri!!))
                val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
                val outputStream = FileOutputStream(file)
                inputStream.copyTo(outputStream)

                if (file.exists()) {
                    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                    body = MultipartBody.Part.createFormData("file", file.name, requestFile)
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택이 취소되었습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private

    fun getBitmap(imageUri: Uri?): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, imageUri!!))
            } else {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap
    }
}