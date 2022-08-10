package com.cool.lib.util

import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.cool.lib.ext.logE

object ViewUtil {
    /**
     * 是否可以点击
     *
     * @param isEnable isEnable
     */
    fun View?.checkViewEnable(isEnable: Boolean) {
        this?.apply {
            isEnabled = isEnable
            checkViewAlpha(isEnable)
        }
    }

    /**
     * 是否透明
     *
     * @param view    view
     * @param isAlpha isAlpha
     */
    fun View?.checkViewAlpha(isAlpha: Boolean) {
        this?.apply {
            alpha = if (isAlpha) 1f else 0.5f
        }
    }

    fun EditText?.checkEditTextEnable(): Boolean {
        this?.apply {
            val trim = text.toString().trim { it <= ' ' }
            if (!TextUtils.isEmpty(trim)) {
                isEnabled = false
                alpha = 0.5f
                return true
            }
        }
        return false
    }

    fun TextView?.checkTextViewEnable(): Boolean {
        this?.apply {
            val trim = text.toString().trim { it <= ' ' }
            if (!TextUtils.isEmpty(trim)) {
                isEnabled = false
                return true
            }
        }
        return false
    }

    fun EditText?.setEditTextFocus(isFocus: Boolean) {
        this?.apply {
            if (isFocus) {
                isCursorVisible = true
                isFocusable = true
                isFocusableInTouchMode = true
                requestFocus()
            } else {
                isCursorVisible = false
                isFocusable = false
                isFocusableInTouchMode = false
            }
        }
    }

    fun TextView?.getTextStringTrim(): String {
        return this.getTextString().trim()

    }

    fun TextView?.getTextString(): String {
        var result = ""
        kotlin.runCatching {
            if (this != null) {
                result = this.text.toString()
            }
        }.onFailure {
            logE(it)
        }
        return result
    }
}