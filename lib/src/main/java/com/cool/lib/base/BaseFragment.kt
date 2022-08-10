package com.cool.lib.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
 * @since 2022/07/22 10:42
 * @version V1.0
 */
abstract class BaseFragment<V : ViewModel, B : ViewDataBinding> : MyBaseFragment(), BaseInitMethod {
    lateinit var viewModel: V
    lateinit var binding: B

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (injectCreate(inflater, container, savedInstanceState)) {
            initView()
            initData()
        } else {
            throw NullPointerException()
        }

        return binding.root
    }

    private fun injectCreate(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): Boolean {
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
                DataBindingUtil.bind(injectRootView)!!
            } else {
                val view = super.onCreateView(inflater, container, savedInstanceState)
                if (view != null) {
                    DataBindingUtil.bind(view)!!
                } else {
                    DataBindingUtil.inflate(inflater, injectLayoutId(), container, false)
                }
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