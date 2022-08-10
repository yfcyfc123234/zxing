package com.cool.lib.http.callback

import android.util.Log
import com.cool.lib.BuildConfig
import com.cool.lib.bean.BaseResultBean
import com.cool.lib.bean.BaseResultPageBean
import com.cool.lib.ext.logE
import com.cool.lib.http.HttpJudgeHelper
import com.cool.lib.http.HttpJudgeHelper.judgeStatus
import com.cool.lib.http.HttpJudgeListener
import com.lzy.okgo.model.Response

abstract class NormalListCallback<T>(
    showProgress: Boolean = false,
    showJudgmentFailedMsg: Boolean = true,
    showNetworkErrorMsg: Boolean = true,
) : HttpProgressCallback<BaseResultPageBean<T>?>(showProgress, showJudgmentFailedMsg, showNetworkErrorMsg) {
    override fun onSuccess(response: Response<BaseResultPageBean<T>?>?) {
        kotlin.runCatching {
            if (isDestroy()) {
                return
            }
            if (response != null) {
                val resultPageBean = response.body()
                if (resultPageBean != null) {
                    judgeStatus(context, resultPageBean.message, resultPageBean.code, object : HttpJudgeListener() {
                        override fun onSuccess(baseResultBean: BaseResultBean<Any?>?, json: String?) {
                            if (!isDestroy()) {
                                onResponseSuccess(resultPageBean, resultPageBean.data)
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
                    myShowErrorMsg("resultPageBean=null")
                }
            } else {
                myShowErrorMsg("response=null")
            }
        }.onFailure {
            logE(it)
            myShowErrorMsg(Log.getStackTraceString(it))
        }
    }

    private fun myShowErrorMsg(errorMsg: String) {
        kotlin.runCatching {
            val errorMsgResult = NormalListCallback::class.java.simpleName + ":" + errorMsg
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

    abstract fun onResponseSuccess(resultPageBean: BaseResultPageBean<T>, list: MutableList<T>?)
    override fun onResponseFailed(response: Response<BaseResultPageBean<T>?>?, code: Int, errorMsg: String?) {}
}