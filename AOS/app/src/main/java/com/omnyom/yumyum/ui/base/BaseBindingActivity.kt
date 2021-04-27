package com.omnyom.yumyum.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
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
}