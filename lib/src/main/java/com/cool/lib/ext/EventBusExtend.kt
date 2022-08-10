@file:JvmName(EVENTBUS_EXTEND)

package com.cool.lib.ext

import com.blankj.utilcode.util.LogUtils
import org.greenrobot.eventbus.EventBus

/**
 *
 * @author yfc
 * @since 2022/07/29 09:17
 * @version V1.0
 */
fun Any?.eventBusRegister() {
    runCatching {
        this?.let {
            if (!getDefaultEventBus().isRegistered(it)) {
                getDefaultEventBus().register(it)
            }
        }
    }.onFailure {
        LogUtils.e(it)
    }
}

fun Any?.eventBusUnRegister() {
    runCatching {
        this?.let {
            if (getDefaultEventBus().isRegistered(it)) {
                getDefaultEventBus().unregister(it)
            }
        }
    }.onFailure {
        LogUtils.e(it)
    }
}

fun getDefaultEventBus(): EventBus {
    return EventBus.getDefault()
}