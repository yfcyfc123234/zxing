package com.cool.lib.bean

open class BaseResultWithCountBean<D>(
    code: Int = 0,
    message: String? = null,
    data: D? = null,
    var count: Int = 0,
) : BaseResultBean<D>(code, message, data) {
    companion object {
        private const val serialVersionUID = 1497071726842009869L
    }
}