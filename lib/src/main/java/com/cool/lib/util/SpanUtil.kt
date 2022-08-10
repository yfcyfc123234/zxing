package com.cool.lib.util

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan

/**
 * SpannableStringBuilder
 * 富文本构建类
 */
class SpanUtil {
    private val flag = Spannable.SPAN_INCLUSIVE_EXCLUSIVE
    private val spannableStringBuilder: SpannableStringBuilder = SpannableStringBuilder()
    private var startIndex: Int
    private var endIndex: Int//开始肯结束胡下标

    init {
        startIndex = 0
        endIndex = 0
    }

    fun appendText(text: String): SpanUtil {
        spannableStringBuilder.append(text)
        val length = text.length
        startIndex = endIndex
        endIndex += length
        return this
    }

    fun setTextColor(textColor: Int): SpanUtil {
        spannableStringBuilder.setSpan(ForegroundColorSpan(textColor), startIndex, endIndex, flag)
        return this
    }

    /**
     * 设置下划线
     *
     * @return SpanUtil
     */
    fun addUnderline(): SpanUtil {
        spannableStringBuilder.setSpan(UnderlineSpan(), startIndex, endIndex, flag)
        return this
    }

    fun setBackgroundColor(color: Int): SpanUtil {
        spannableStringBuilder.setSpan(BackgroundColorSpan(color), startIndex, endIndex, flag)
        return this
    }

    fun setTextSize(size_px: Int): SpanUtil {
        spannableStringBuilder.setSpan(AbsoluteSizeSpan(size_px), startIndex, endIndex, flag)
        return this
    }

    fun build(): SpannableStringBuilder {
        return spannableStringBuilder
    }
}