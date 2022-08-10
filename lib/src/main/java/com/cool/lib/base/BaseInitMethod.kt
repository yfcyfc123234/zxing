package com.cool.lib.base

import android.view.View

/**
 *
 * @author yfc
 * @since 2022/07/22 11:17
 * @version V1.0
 */
interface BaseInitMethod {
    fun injectLayoutId(): Int

    fun initView()

    fun initData()

    fun injectRootView(): View? {
        return null
    }
}