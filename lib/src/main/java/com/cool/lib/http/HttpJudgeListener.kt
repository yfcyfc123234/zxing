package com.cool.lib.http

import com.cool.lib.bean.BaseResultBean

abstract class HttpJudgeListener {
    abstract fun onSuccess(resultBean: BaseResultBean<Any?>?, json: String?)

    abstract fun onFailed(code: Int, errorMsg: String?)

    open fun onIntercept(code: Int, msg: String?) {}
}