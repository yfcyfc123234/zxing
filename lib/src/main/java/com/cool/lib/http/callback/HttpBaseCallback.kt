package com.cool.lib.http.callback

import android.text.TextUtils
import com.blankj.utilcode.util.GsonUtils
import com.cool.lib.bean.BaseResultBean
import com.cool.lib.bean.BaseResultPageBean
import com.cool.lib.ext.logE
import com.cool.lib.util.LoggerUtil
import com.google.gson.reflect.TypeToken
import com.lzy.okgo.callback.AbsCallback
import okhttp3.Response
import okhttp3.ResponseBody
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*

abstract class HttpBaseCallback<T> : AbsCallback<T> {
    private var myType: Type? = null
    var type: Type? = null
    var clazz: Class<T>? = null

    var rawData: String? = null

    constructor() {
        this.myType = handleMyType()
    }

    constructor(type: Type?) {
        this.type = type
    }

    constructor(clazz: Class<T>?) {
        this.clazz = clazz
    }

    override fun convertResponse(response: Response): T? {
        var result: T? = null
        kotlin.runCatching {
            val body = response.body
            if (body != null) {
                getRawData(body)
                val reader = body.charStream()
                if (type != null) {
                    result = GsonUtils.fromJson(reader, type!!)
                } else if (clazz != null) {
                    result = GsonUtils.fromJson(reader, clazz!!)
                } else {
                    kotlin.runCatching {
                        result = if (this@HttpBaseCallback is OtherCallback<*>) {
                            GsonUtils.fromJson(reader, myType!!)
                        } else {
                            val o = GsonUtils.fromJson<Any>(reader, object : TypeToken<T>() {}.type)
                            val map = o as? AbstractMap<*, *>
                            handleMap(map)
                        }
                    }.onFailure {
                        logE(it)
                    }
                }
                if (result == null) {
                    val s = rawData
                    if (!TextUtils.isEmpty(s)) {
                        if (type != null) {
                            result = GsonUtils.fromJson(s, type!!)
                        } else if (clazz != null) {
                            result = GsonUtils.fromJson(s, clazz!!)
                        } else {
                            kotlin.runCatching {
                                result = if (this@HttpBaseCallback is OtherCallback<*>) {
                                    GsonUtils.fromJson(s, myType!!)
                                } else {
                                    val o = GsonUtils.fromJson<Any>(s, object : TypeToken<T>() {}.type)
                                    val map = o as? AbstractMap<*, *>
                                    handleMap(map)
                                }
                            }.onFailure {
                                logE(it)
                            }
                        }
                    }
                }
            }
        }.onFailure {
            logE(it)
        }
        return result
    }

    private fun getRawData(body: ResponseBody?) {
        try {
            if (body != null) {
                rawData = body.string()
            }
        } catch (ignored: Exception) {
        }
    }

    private fun handleMyType(): Type {
        val genType = javaClass.genericSuperclass
        return (Objects.requireNonNull(genType) as ParameterizedType).actualTypeArguments[0]
    }

    private fun handleMap(map: AbstractMap<*, *>?): T? {
        if (map != null) {
            if (this@HttpBaseCallback is NormalCallback<*>) {
                val bean = BaseResultBean<Any>()
                val code = map["code"]
                if (code != null) {
                    if (code is Number) {
                        bean.code = code.toInt()
                    } else {
                        bean.code = code.toString().toInt()
                    }
                }
                val message = map["msg"]
                if (message != null) {
                    bean.message = message as String?
                }
                kotlin.runCatching {
                    val data = map["data"]
                    if (data != null) {
                        val json = GsonUtils.toJson(data)
                        bean.data = GsonUtils.fromJson(json, myType!!)
                    }
                }.onFailure {
                    logE(it)
                }
                return bean as T
            } else if (this@HttpBaseCallback is NormalListCallback<*>) {
                val bean = BaseResultPageBean<Any>()
                val code = map["code"]
                if (code != null) {
                    if (code is Number) {
                        bean.code = code.toInt()
                    } else {
                        bean.code = code.toString().toInt()
                    }
                }
                val message = map["msg"]
                if (message != null) {
                    bean.message = message as? String
                }
                val count = map["count"]
                if (count != null) {
                    bean.count = 0
                    if (count is String) {
                        bean.count = count.toInt()
                    } else {
                        try {
                            bean.count = count as Int
                        } catch (e: Exception) {
                            LoggerUtil.e(e)
                        }
                    }
                }
                kotlin.runCatching {
                    val data = map["data"]
                    if (data is List<*>) {
                        val list = data as? List<*>
                        bean.data = ArrayList()
                        list?.forEach {
                            val json = GsonUtils.toJson(it)
                            bean.data!!.add(GsonUtils.fromJson(json, myType!!))
                        }
                    }
                }.onFailure {
                    logE(it)
                }
                return bean as T
            }
        }
        return null
    }
}