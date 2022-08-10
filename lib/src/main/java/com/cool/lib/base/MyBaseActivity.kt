package com.cool.lib.base

import android.os.Bundle
import android.text.TextUtils
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import com.cool.lib.bean.ClassEvent
import com.cool.lib.ext.eventBusRegister
import com.cool.lib.ext.eventBusUnRegister
import com.cool.lib.ext.logE
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class MyBaseActivity : AppCompatActivity() {
    open fun isNeedEventBus(): Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isNeedEventBus()) {
            this.eventBusRegister()
        }
    }

    override fun onDestroy() {
        if (isNeedEventBus()) {
            this.eventBusUnRegister()
        }
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @CallSuper
    fun onClassEvent(classEvent: ClassEvent?) {
        kotlin.runCatching {
            if (!isDestroyed && classEvent != null) {
                if (classEvent.type == ClassEvent.TYPE_FINISH_ALL) {
                    try {
                        var isExceptClass = false
                        if (classEvent.obj is List<*>) {
                            val classes = classEvent.obj as List<*>
                            for (aClass in classes) {
                                if (aClass == this::javaClass) {
                                    isExceptClass = true
                                    break
                                }
                            }
                        }
                        if (!isExceptClass) {
                            finish()
                        }
                    } catch (e: Exception) {
                        logE(e)
                    }
                } else if (TextUtils.equals(classEvent.className, javaClass.name)) {
                    when (classEvent.type) {
                        ClassEvent.TYPE_FINISH -> finish()
                        ClassEvent.TYPE_REFRESH -> onNeedRefresh()
                        ClassEvent.TYPE_OTHER -> onHandleClassEvent(classEvent)
                    }
                }
            }
        }.onFailure {
            logE(it)
        }
    }

    @CallSuper
    open fun onNeedRefresh() {
    }

    @CallSuper
    open fun onHandleClassEvent(classEvent: ClassEvent) {
    }
}