package com.omnyom.yumyum.ui.camera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.omnyom.yumyum.BaseActivity
import com.omnyom.yumyum.databinding.ActivityCameraBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CameraActivity : BaseActivity() {
    val PERM_STORAGE = 99 // 외부 저장소 권한 처리
    val PERM_CAMERA = 100 // 카메라 권한처리
    val REQ_CAMERA = 101 // 카메라 촬영 요청

    val binding by lazy { ActivityCameraBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        requirePermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERM_STORAGE)
    }

    override fun permissionGranted(requestCode: Int) {
        when (requestCode) {
            PERM_STORAGE -> setViews()
            PERM_CAMERA -> openCamera()
        }
    }

    override fun permissionDenied(requestCode: Int) {
        when (requestCode) {
            PERM_STORAGE -> {
                Toast.makeText(baseContext, "외부 저장소 권한을 승인해야 앱을 사용할 수 있습니다!?", Toast.LENGTH_SHORT).show()
                finish()
            }
            PERM_CAMERA -> {
                Toast.makeText(baseContext, "카메라 권한을 승인해야 카메라를 사용할 수 있습니다!?", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun setViews() {
        binding.btnCamera.setOnClickListener{
            requirePermissions(arrayOf(Manifest.permission.CAMERA), PERM_CAMERA)
        }
    }

    fun openCamera() {
        var intent : Intent =  Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 3);
        intent.also { takeVideoIntent ->
            takeVideoIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takeVideoIntent, REQ_CAMERA)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                REQ_CAMERA -> {
                    binding.videoPreview.setVideoURI(data?.data)
                    binding.btnCamera.visibility = View.GONE
                    binding.videoPreview.visibility = View.VISIBLE
                    binding.btnNext.visibility = View.VISIBLE
                    binding.videoPreview.setOnPreparedListener {mp ->
                        binding.videoPreview.start()
                        mp!!.isLooping = true

                    }



                }
            }
    }

}