package com.cool.lib.http

import android.content.Context
import androidx.fragment.app.Fragment
import com.cool.lib.bean.BaseResultBean
import com.cool.lib.bean.BaseResultPageBean
import com.cool.lib.http.callback.*
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.HttpHeaders
import com.lzy.okgo.model.HttpParams
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

object OkGoCompat {
    private const val MEDIA_TYPE_JSON = "application/json; charset=utf-8"

    var listener: OkGoListener? = null

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////get///////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    operator fun <D> get(context: Context, url: String?, httpParams: HttpParams?, callback: NormalCallback<D>) {
        handleCallback(context, callback)
        OkGo.get<BaseResultBean<D>>(url)
            .headers(getHttpHeaders(context))
            .params(httpParams)
            .execute(callback)
    }

    operator fun <D> get(fragment: Fragment, url: String?, httpParams: HttpParams?, callback: NormalCallback<D>) {
        handleCallback(fragment, callback)
        OkGo.get<BaseResultBean<D>>(url)
            .headers(getHttpHeaders(fragment.requireContext()))
            .params(httpParams)
            .execute(callback)
    }

    fun <D> getOther(context: Context, url: String?, httpParams: HttpParams?, callback: OtherCallback<D?>) {
        handleCallback(context, callback)
        OkGo.get<D>(url)
            .headers(getHttpHeaders(context))
            .params(httpParams)
            .execute(callback)
    }

    fun <D> getOther(fragment: Fragment, url: String?, httpParams: HttpParams?, callback: OtherCallback<D?>) {
        handleCallback(fragment, callback)
        OkGo.get<D>(url)
            .headers(getHttpHeaders(fragment.requireContext()))
            .params(httpParams)
            .execute(callback)
    }

    fun <D> getPageList(context: Context, url: String?, httpParams: HttpParams?, callback: NormalListCallback<D>) {
        handleCallback(context, callback)
        OkGo.get<BaseResultPageBean<D>>(url)
            .headers(getHttpHeaders(context))
            .params(httpParams)
            .execute(callback)
    }

    fun <D> getPageList(fragment: Fragment, url: String?, httpParams: HttpParams?, callback: NormalListCallback<D>) {
        handleCallback(fragment, callback)
        OkGo.get<BaseResultPageBean<D>>(url)
            .headers(getHttpHeaders(fragment.requireContext()))
            .params(httpParams)
            .execute(callback)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////post//////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    fun <D> post(context: Context, url: String?, httpParams: HttpParams?, callback: NormalCallback<D>) {
        handleCallback(context, callback)
        OkGo.post<BaseResultBean<D>>(url)
            .headers(getHttpHeaders(context))
            .params(httpParams)
            .execute(callback)
    }

    fun <D> post(fragment: Fragment, url: String?, httpParams: HttpParams?, callback: NormalCallback<D>) {
        handleCallback(fragment, callback)
        OkGo.post<BaseResultBean<D>>(url)
            .headers(getHttpHeaders(fragment.requireContext()))
            .params(httpParams)
            .execute(callback)
    }

    fun <D> postOther(context: Context, url: String?, httpParams: HttpParams?, callback: OtherCallback<D?>) {
        handleCallback(context, callback)
        OkGo.post<D>(url)
            .headers(getHttpHeaders(context))
            .params(httpParams)
            .execute(callback)
    }

    fun <D> postOther(fragment: Fragment, url: String?, httpParams: HttpParams?, callback: OtherCallback<D?>) {
        handleCallback(fragment, callback)
        OkGo.post<D>(url)
            .headers(getHttpHeaders(fragment.requireContext()))
            .params(httpParams)
            .execute(callback)
    }

    fun <D> postPageList(context: Context, url: String?, httpParams: HttpParams?, callback: NormalListCallback<D>) {
        handleCallback(context, callback)
        OkGo.post<BaseResultPageBean<D>>(url)
            .headers(getHttpHeaders(context))
            .params(httpParams)
            .execute(callback)
    }

    fun <D> postPageList(fragment: Fragment, url: String?, httpParams: HttpParams?, callback: NormalListCallback<D>) {
        handleCallback(fragment, callback)
        OkGo.post<BaseResultPageBean<D>>(url)
            .headers(getHttpHeaders(fragment.requireContext()))
            .params(httpParams)
            .execute(callback)
    }

    fun <D> postPageListContent(context: Context, url: String?, content: String, callback: NormalListCallback<D>) {
        handleCallback(context, callback)
        OkGo.post<BaseResultPageBean<D>>(url)
            .headers(getHttpHeaders(context))
            .upRequestBody(getRequestBody(content))
            .execute(callback)
    }

    fun <D> postPageListContent(fragment: Fragment, url: String?, content: String, callback: NormalListCallback<D>) {
        handleCallback(fragment, callback)
        OkGo.post<BaseResultPageBean<D>>(url)
            .headers(getHttpHeaders(fragment.requireContext()))
            .upRequestBody(getRequestBody(content))
            .execute(callback)
    }

    fun <D> postContent(context: Context, url: String?, content: String, callback: NormalCallback<D>) {
        handleCallback(context, callback)
        OkGo.post<BaseResultBean<D>>(url)
            .headers(getHttpHeaders(context))
            .upRequestBody(getRequestBody(content))
            .execute(callback)
    }

    fun <D> postContent(fragment: Fragment, url: String?, content: String, callback: NormalCallback<D>) {
        handleCallback(fragment, callback)
        OkGo.post<BaseResultBean<D>>(url)
            .headers(getHttpHeaders(fragment.requireContext()))
            .upRequestBody(getRequestBody(content))
            .execute(callback)
    }

    fun <D> postOtherContent(context: Context, url: String?, content: String, callback: OtherCallback<D?>) {
        handleCallback(context, callback)
        OkGo.post<D>(url)
            .headers(getHttpHeaders(context))
            .upRequestBody(getRequestBody(content))
            .execute(callback)
    }

    fun <D> postOtherContent(fragment: Fragment, url: String?, content: String, callback: OtherCallback<D?>) {
        handleCallback(fragment, callback)
        OkGo.post<D>(url)
            .headers(getHttpHeaders(fragment.requireContext()))
            .upRequestBody(getRequestBody(content))
            .execute(callback)
    }

    private fun getRequestBody(content: String) = content.toRequestBody(MEDIA_TYPE_JSON.toMediaTypeOrNull())

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private fun getHttpHeaders(context: Context?): HttpHeaders {
        var params: HttpHeaders? = null
        if (listener != null) {
            params = listener!!.httpHeaders()
        }
        if (params == null) {
            params = HttpHeaders()
        }
        return params
    }

    private fun <D> handleCallback(context: Context, callback: HttpProgressCallback<D?>) {
        callback.context = context
    }

    private fun <D> handleCallback(fragment: Fragment, callback: HttpProgressCallback<D?>) {
        callback.fragment = fragment
    }
}