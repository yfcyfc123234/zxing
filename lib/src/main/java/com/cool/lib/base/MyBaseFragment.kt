package com.cool.lib.base

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.cool.lib.bean.ClassEvent
import com.cool.lib.ext.eventBusRegister
import com.cool.lib.ext.eventBusUnRegister
import com.cool.lib.ext.logE
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class MyBaseFragment : Fragment() {
    open fun isNeedEventBus(): Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        if (isNeedEventBus()) {
            this.eventBusRegister()
        }
        return view
    }

    override fun onDestroyView() {
        if (isNeedEventBus()) {
            this.eventBusUnRegister()
        }
        super.onDestroyView()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @CallSuper
    fun onClassEvent(classEvent: ClassEvent?) {
        kotlin.runCatching {
            if (!isDetached && !isRemoving && classEvent != null) {
                if (TextUtils.equals(classEvent.className, javaClass.name)) {
                    when (classEvent.type) {
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