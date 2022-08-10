@file:JvmName(LOG_EXTEND)

package com.cool.lib.ext

import android.content.Context
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.cool.lib.BuildConfig

/**
 *
 * @author yfc
 * @since 2022/07/29 09:10
 * @version V1.0
 */
fun showToast(msg: String?) {
    if (!msg.isNullOrEmpty()) {
        ToastUtils.showLong(msg)
    }
}

fun logV(message: Any? = null, tag: String = BASE_TAG) {
    log(message, tag, LogUtils.V)
}

fun logD(message: Any? = null, tag: String = BASE_TAG) {
    log(message, tag, LogUtils.D)
}

fun logI(message: Any? = null, tag: String = BASE_TAG) {
    log(message, tag, LogUtils.I)
}

fun logW(message: Any? = null, tag: String = BASE_TAG) {
    log(message, tag, LogUtils.W)
}

fun logE(message: Any? = null, tag: String = BASE_TAG) {
    log(message, tag, LogUtils.E)
}

fun logA(message: Any? = null, tag: String = BASE_TAG) {
    log(message, tag, LogUtils.A)
}

fun setTextColor(@ColorRes id: Int, context: Context, vararg textViews: TextView) {
    textViews.forEach {
        it.setTextColor(ContextCompat.getColor(context, id))
    }
}

private fun log(message: Any? = null, tag: String = BASE_TAG, level: Int = LogUtils.E) {
    if (BuildConfig.DEBUG) {
        when (level) {
            LogUtils.V -> {
                LogUtils.vTag(tag, message)
            }
            LogUtils.D -> {
                LogUtils.dTag(tag, message)
            }
            LogUtils.I -> {
                LogUtils.iTag(tag, message)
            }
            LogUtils.W -> {
                LogUtils.wTag(tag, message)
            }
            LogUtils.E -> {
                LogUtils.eTag(tag, message)
            }
            LogUtils.A -> {
                LogUtils.aTag(tag, message)
            }
            else -> {
                LogUtils.eTag(tag, message)
            }
        }
    }
}