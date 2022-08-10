package com.cool.lib.bean

open class BaseResultPageBean<D>(
    code: Int = 0,
    message: String? = null,
    data: MutableList<D>? = null,
    var count: Int = 0,
) : BaseResultBean<MutableList<D>>(code, message, data) {
    companion object {
        private const val serialVersionUID = -4813908396243170292L
    }
}