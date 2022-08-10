package com.cool.lib.http

import android.content.Context
import android.util.Log
import com.cool.lib.BuildConfig
import com.cool.lib.ext.logE

object HttpJudgeHelper {
    /**
     * 网络错误
     */
    const val NETWORK_ERROR = -9527
    const val NETWORK_ERROR_MSG = "网络连接失败"

    /**
     * 程序处理数据时出现错误
     */
    const val IO_ERROR = -9528
    const val IO_ERROR_MSG = "网络访问失败"

    /**
     * 系统错误
     */
    const val SERVER_ERROR = -1

    /**
     * 请求成功
     */
    const val SUCCESS = 200

    /**
     * 单点登录
     */
    const val SINGAL_POINT = 99999
    const val SINGAL_POINT2 = 10105

    @JvmStatic
    fun judgeStatus(context: Context?, msg: String?, code: Int, listener: HttpJudgeListener) {
        kotlin.runCatching {
            when (code) {
                SUCCESS -> listener.onSuccess(null, null)
                SINGAL_POINT, SINGAL_POINT2 -> {
                    listener.onFailed(code, msg)
                    OkGoCompat.listener?.logout()
                }
                SERVER_ERROR -> listener.onFailed(code, msg)
                else -> listener.onFailed(code, msg)
            }
        }.onFailure {
            logE(it)
            val errorMsg: String = if (BuildConfig.DEBUG) {
                "judgeStatusError errorMsg=" + Log.getStackTraceString(it)
            } else {
                IO_ERROR_MSG
            }
            listener.onFailed(IO_ERROR, errorMsg)
        }
    }
}