package com.cool.lib.http.callback

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.cool.lib.BuildConfig
import com.cool.lib.http.HttpJudgeHelper
import com.cool.lib.http.HttpLoadingDialog
import com.lzy.okgo.model.Response
import com.lzy.okgo.request.base.Request

abstract class HttpProgressCallback<T>(
    open var showProgress: Boolean = true, //是否需要显示加载框
    open var showJudgmentFailedMsg: Boolean = true, //是否需要显示业务逻辑失败消息
    open var showNetworkErrorMsg: Boolean = true,//是否需要显示网络错误消息
) : HttpBaseCallback<T>() {
    var context: Context? = null
    var fragment: Fragment? = null

    abstract fun onResponseFailed(response: Response<T?>?, code: Int, errorMsg: String?)

    fun onResponseIntercept(code: Int, msg: String?) {}

    override fun onStart(request: Request<T?, out Request<Any, Request<*, *>>>?) {
        super.onStart(request)

        if (context == null) {
            context = if (fragment != null) {
                fragment!!.requireContext()
            } else {
                ActivityUtils.getTopActivity()
            }
        }
        if (showProgress) {
            HttpLoadingDialog.INSTANCE.addLoading(context!!)
        }
    }

    override fun onError(response: Response<T?>?) {
        super.onError(response)

        if (!isDestroy()) {
            val errorMsg: String = if (BuildConfig.DEBUG) {
                Log.getStackTraceString(response?.exception)
            } else {
                HttpJudgeHelper.NETWORK_ERROR_MSG
            }
            showErrorMsg(errorMsg, showNetworkErrorMsg)
            onResponseFailed(response, HttpJudgeHelper.NETWORK_ERROR, errorMsg)
        }
    }

    override fun onFinish() {
        super.onFinish()

        if (showProgress) {
            HttpLoadingDialog.INSTANCE.removeLoading()
        }
    }

    fun showErrorMsg(errorMsg: String?, needShow: Boolean) {
        if (!isDestroy()) {
            if (needShow && !TextUtils.isEmpty(errorMsg)) {
                ToastUtils.showLong(errorMsg)
            }
        }
    }

    fun isDestroy(): Boolean {
        if (fragment != null) {
            return fragment!!.isRemoving || fragment!!.isDetached
        } else if (context != null) {
            if (context is Activity) {
                val activity = context as Activity?
                return activity!!.isFinishing || activity.isDestroyed
            }
        }
        return false
    }
}