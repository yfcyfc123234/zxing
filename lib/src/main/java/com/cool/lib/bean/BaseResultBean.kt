package com.cool.lib.bean

import com.cool.lib.http.HttpJudgeHelper

open class BaseResultBean<D>(
    var code: Int = 0,
    var message: String? = null,
    var data: D? = null,
) : BaseBean() {
    companion object {
        private const val serialVersionUID = -9072064201304443308L
    }

    open fun isSuccess(): Boolean {
        return code == HttpJudgeHelper.SUCCESS
    }
}