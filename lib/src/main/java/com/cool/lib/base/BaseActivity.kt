package com.cool.lib.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ReflectUtils
import com.cool.lib.ext.logE
import java.lang.reflect.ParameterizedType
import java.util.*

/**
 *
 * @author yfc
 * @since 2022/07/06 11:59
 * @version V1.0
 */
abstract class BaseActivity<V : ViewModel, B : ViewDataBinding> : MyBaseActivity(), BaseInitMethod {
    lateinit var viewModel: V
    lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (injectCreate()) {
            initView()
            initData()
        } else {
            finish()
        }
    }

    private fun injectCreate(): Boolean {
        kotlin.runCatching {
            val type = javaClass.genericSuperclass
            if (type is ParameterizedType) {
                val actualType = type.actualTypeArguments[0]
                if (actualType is Class<*>) {
                    if (ViewModel::class.java.isAssignableFrom(actualType)) {
                        viewModel = ViewModelProvider(this)[actualType as Class<V>]
                    }
                }
            }
            Objects.requireNonNull(viewModel)
            val injectRootView = injectRootView()
            binding = if (injectRootView != null) {
                setContentView(injectRootView)
                DataBindingUtil.bind(injectRootView)!!
            } else {
                DataBindingUtil.setContentView(this, injectLayoutId())
            }
            binding.lifecycleOwner = this
            kotlin.runCatching { ReflectUtils.reflect(binding).method("setViewModel", viewModel) }
            Objects.requireNonNull(binding)
            return true
        }.onFailure {
            logE(it)
        }
        return false
    }
}