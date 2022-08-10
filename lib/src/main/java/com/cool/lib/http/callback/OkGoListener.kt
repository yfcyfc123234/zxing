package com.cool.lib.http.callback

import com.lzy.okgo.model.HttpHeaders

interface OkGoListener {
    fun httpHeaders(): HttpHeaders

    fun logout()
}