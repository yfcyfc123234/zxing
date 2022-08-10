package com.cool.lib.util

import android.content.Context
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

object GlideUtils {
    private const val TAG = "GlideUtils"

    /**
     * @param context context
     * @param url     url
     * @param iv      正常举行图片
     */
    fun loadCommonImage(context: Context?, url: String?, iv: ImageView?) {
        if (TextUtils.isEmpty(url)) {
            return
        }
        if (context != null) {
            Glide.with(context)
                .load(url)
                .into(iv!!)
        }
    }

    /**
     * @param context context
     * @param url     url
     * @param iv      正常举行图片
     */
    fun loadCommonImage(context: Context?, url: Any?, iv: ImageView?) {
        Glide.with(context!!)
            .load(url)
            .into(iv!!)
    }

    fun loadCommonImage(context: Context?, url: String?, msg: String?, iv: ImageView?) {
        if (TextUtils.isEmpty(url)) {
            return
        }
        if (context != null) {
            Glide.with(context)
                .load(url)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv!!)
        }
    }

    /**
     * @param context context
     * @param url     url
     * @param iv      加载圆图片
     */
    fun loadCircleImage(context: Context?, url: String?, iv: ImageView?) {
        if (TextUtils.isEmpty(url)) {
            return
        }
        val circleCrop = CircleCrop()
        val options = RequestOptions.bitmapTransform(circleCrop)
        Glide.with(context!!)
            .load(url)
            .apply(options)
            .into(iv!!)
    }

    /**
     * @param context context
     * @param url     url
     * @param iv      iv
     * @param radios  圆角大小
     * 圆角图片
     */
    fun loadRoundImage(context: Context?, url: String?, iv: ImageView?, radios: Int) {
        if (TextUtils.isEmpty(url)) {
            return
        }
        val corners = RoundedCorners(radios)
        val options = RequestOptions.bitmapTransform(corners)
        Glide.with(context!!)
            .load(url)
            .apply(options)
            .into(iv!!)
    }

    fun loadCommonImageCompat(context: Context?, url: Any?, errorResId: Int, iv: ImageView?) {
        Glide.with(context!!)
            .load(url)
            .error(errorResId)
            .into(iv!!)
    }

    fun loadRoundImageCompat(context: Context?, url: String?, errorResId: Int, iv: ImageView?, radios: Int) {
        val corners = RoundedCorners(radios)
        val options = RequestOptions.bitmapTransform(corners)
        Glide.with(context!!)
            .load(url)
            .error(errorResId)
            .apply(options)
            .into(iv!!)
    }

    /**
     * @param context context
     * @param res     res
     * @param iv      iv
     * @param radios  圆角大小
     * 圆角图片
     */
    fun loadRoundImage(context: Context?, res: Int, iv: ImageView?, radios: Int) {
        val corners = RoundedCorners(radios)
        val options = RequestOptions.bitmapTransform(corners)
        Glide.with(context!!) //                .load(R.mipmap.ic_del)
            .load(res)
            .apply(options)
            .into(iv!!)
    }
}