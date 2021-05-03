package com.omnyom.yumyum.ui.base

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseBindingActivity<T : ViewDataBinding>(
        @LayoutRes private val contentLayoutId: Int
) : AppCompatActivity() {

    private var _binding: T? = null
    protected val binding: T
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, contentLayoutId)
        extraSetupBinding()
        setup()
        setupViews()
        onSubscribe()
    }

    /** dataBinding 객체에 추가적인 작업을 할 때 사용한다 */
    protected abstract fun extraSetupBinding()

    /** ui와 관련 없는 요소를 초기화 할 때 사용한다 */
    protected abstract fun setup()

    /** ui를 초기화 할 때 사용한다 */
    protected abstract fun setupViews()

    /** livedata에 subscribe할 때 사용한다 */
    protected abstract fun onSubscribe()


    override fun onDestroy() {
        release()
        _binding = null
        super.onDestroy()
    }

    protected abstract fun release()

    open fun permissionGranted(requestCode: Int) {}
    open fun permissionDenied(requestCode: Int) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            permissionGranted(requestCode)
        } else {
            permissionDenied(requestCode)
        }
    }

    fun requirePermissions(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            permissionGranted(requestCode)
        } else {
            val isAllPermissionsGranted = permissions.all {
                checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
            }
            if (isAllPermissionsGranted) {
                permissionGranted(requestCode)
            } else {
                ActivityCompat.requestPermissions(this, permissions, requestCode)
            }
        }
    }
}