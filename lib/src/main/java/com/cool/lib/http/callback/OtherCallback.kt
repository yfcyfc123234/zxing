package com.cool.lib.http.callback

import android.util.Log
import com.cool.lib.BuildConfig
import com.cool.lib.bean.BaseResultBean
import com.cool.lib.ext.logE
import com.cool.lib.http.HttpJudgeHelper
import com.cool.lib.http.HttpJudgeHelper.judgeStatus
import com.cool.lib.http.HttpJudgeListener
import com.lzy.okgo.model.Response

abstract class OtherCallback<T>(
    var judgmentNull: Boolean = false,//是否需要判断返回数据为空的情况
    var judgmentStatus: Boolean = false, //返回数据不为空的时候是否需要判断的code的情况
    showProgress: Boolean = true,
    showJudgmentFailedMsg: Boolean = true,
    showNetworkErrorMsg: Boolean = true,
) : HttpProgressCallback<T>(showProgress, showJudgmentFailedMsg, showNetworkErrorMsg) {
    override fun onSuccess(response: Response<T?>?) {
        kotlin.runCatching {
            if (isDestroy()) {
                return
            }
            if (response != null) {
                val bean = response.body()
                if (bean != null) {
                    if (judgmentStatus && bean is BaseResultBean<*>) {
                        judgeStatus(context, (bean as BaseResultBean<*>).message, (bean as BaseResultBean<*>).code,
                            object : HttpJudgeListener() {
                                override fun onSuccess(baseResultBean: BaseResultBean<Any?>?, json: String?) {
                                    if (!isDestroy()) {
                                        onResponseSuccess(bean)
                                    }
                                }

                                override fun onIntercept(code: Int, msg: String?) {
                                    if (!isDestroy()) {
                                        onResponseIntercept(code, msg)
                                    }
                                }

                                override fun onFailed(code: Int, errorMsg: String?) {
                                    if (!isDestroy()) {
                                        showErrorMsg(errorMsg, showJudgmentFailedMsg)
                                        onResponseFailed(response, code, errorMsg)
                                    }
                                }
                            })
                    } else {
                        onResponseSuccess(bean)
                    }
                } else {
                    if (judgmentNull) {
                        myShowErrorMsg("bean=null")
                    } else {
                        onResponseSuccess(null)
                    }
                }
            } else {
                if (judgmentNull) {
                    myShowErrorMsg("response=null")
                } else {
                    onResponseSuccess(null)
                }
            }
        }.onFailure {
            logE(it)
            myShowErrorMsg(Log.getStackTraceString(it))
        }
    }

    private fun myShowErrorMsg(errorMsg: String) {
        kotlin.runCatching {
            val errorMsgResult = OtherCallback::class.java.simpleName + ":" + errorMsg
            if (BuildConfig.DEBUG) {
                showErrorMsg(errorMsgResult, showNetworkErrorMsg)
            } else {
                showErrorMsg(HttpJudgeHelper.IO_ERROR_MSG, showNetworkErrorMsg)
            }
            onResponseFailed(null, HttpJudgeHelper.IO_ERROR, HttpJudgeHelper.IO_ERROR_MSG)
        }.onFailure {
            logE(it)
        }
    }

    abstract fun onResponseSuccess(bean: T?)
    override fun onResponseFailed(response: Response<T?>?, code: Int, errorMsg: String?) {}
}