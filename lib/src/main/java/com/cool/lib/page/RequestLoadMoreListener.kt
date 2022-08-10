package com.cool.lib.page

abstract class RequestLoadMoreListener<T> {
    abstract fun onLoadData(pageNum: Int, pageSize: Int)

    fun handleData(pageNum: Int, pageSize: Int, list: MutableList<T>?): MutableList<T>? {
        return list
    }
}