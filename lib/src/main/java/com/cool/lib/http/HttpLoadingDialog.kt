package com.cool.lib.http

import android.content.Context
import com.cool.lib.ext.logE
import com.cool.lib.weight.BaseSimpleProgressDialog

enum class HttpLoadingDialog {
    INSTANCE;

    private var mBackupContext: Context? = null
    private var loadingQueue = 0
    private var mLoadingDialog: BaseSimpleProgressDialog? = null

    fun addLoading(context: Context) {
        if (mBackupContext != null && mBackupContext === context) {
            loadingQueue++
        } else {
            loadingQueue = 1
            mBackupContext = context
        }
        if (loadingQueue == 1) {
            dismiss()
            mLoadingDialog = BaseSimpleProgressDialog(mBackupContext, true)
            show()
        }
    }

    fun removeLoading() {
        loadingQueue--
        if (loadingQueue < 0) {
            loadingQueue = 0
        }
        if (loadingQueue == 0) {
            dismiss()
            clear()
        }
    }

    fun removeAllLoading() {
        loadingQueue = 0
        dismiss()
        clear()
    }

    private fun clear() {
        mBackupContext = null
        mLoadingDialog = null
    }

    private fun show() {
        kotlin.runCatching {
            if (mLoadingDialog != null && !mLoadingDialog!!.isShowing) {
                mLoadingDialog!!.show()
            }
        }.onFailure {
            logE(it)
        }
    }

    private fun dismiss() {
        kotlin.runCatching {
            if (mLoadingDialog != null && mLoadingDialog!!.isShowing) {
                mLoadingDialog!!.dismiss()
            }
        }.onFailure {
            logE(it)
        }
    }
}