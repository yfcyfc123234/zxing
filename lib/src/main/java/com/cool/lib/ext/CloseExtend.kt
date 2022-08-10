@file:JvmName(CLOSE_EXTEND)

package com.cool.lib.ext

import com.blankj.utilcode.util.LogUtils
import io.reactivex.disposables.Disposable
import java.io.Closeable

/**
 *
 * @author yfc
 * @since 2022/07/29 09:14
 * @version V1.0
 */
fun Disposable?.closeSafe() {
    kotlin.runCatching {
        if (this != null && !this.isDisposed) {
            this.dispose()
        }
    }.onFailure {
        LogUtils.e(it)
    }
}

fun io.reactivex.rxjava3.disposables.Disposable?.closeSafe() {
    kotlin.runCatching {
        if (this != null && !this.isDisposed) {
            this.dispose()
        }
    }.onFailure {
        LogUtils.e(it)
    }
}

fun Closeable?.closeSafe() {
    kotlin.runCatching {
        this?.close()
    }.onFailure {
        LogUtils.e(it)
    }
}