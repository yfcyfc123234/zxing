@file:JvmName(VIEW_EXTEND)

package com.cool.lib.ext

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

/**
 *
 * @author yfc
 * @since 2022/07/29 09:12
 * @version V1.0
 */
fun clearText(vararg textViews: TextView) {
    setText("", *textViews)
}

fun setText(text: String?, vararg textViews: TextView) {
    textViews.forEach {
        it.text = text
    }
}

fun setHintTextColor(@ColorRes id: Int, context: Context, vararg editTexts: EditText) {
    editTexts.forEach {
        it.setHintTextColor(ContextCompat.getColor(context, id))
    }
}

fun setAllViewVisibility(view: ViewGroup) {
    for (index in 0 until view.childCount) {
        val childView = view.getChildAt(index)
        if (childView is ViewGroup) {
            setAllViewVisibility(childView)
        } else {
            childView.visibility = View.VISIBLE
        }
    }
}

fun setViewsVisibility(visible: Boolean, vararg views: View) {
    views.forEach {
        it.visibility = if (visible) View.VISIBLE else View.GONE
    }
}

fun setViewsEnabled(isEnabled: Boolean, vararg views: View) {
    views.forEach {
        it.isEnabled = isEnabled
    }
}