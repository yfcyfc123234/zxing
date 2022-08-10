package com.cool.lib.page

import android.view.View
import androidx.annotation.ColorRes
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class RefreshLayout(var swipeRefreshLayout: SwipeRefreshLayout? = null, var smartRefreshLayout: SmartRefreshLayout? = null) {
    init {
        smartRefreshLayout?.setEnableLoadMore(false)
    }

    val refreshLayout: View?
        get() = if (swipeRefreshLayout != null) {
            swipeRefreshLayout
        } else if (smartRefreshLayout != null) {
            smartRefreshLayout
        } else {
            null
        }

    fun setRefreshing(refreshing: Boolean) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout!!.isRefreshing = refreshing
        } else if (smartRefreshLayout != null) {
            if (refreshing) {
                smartRefreshLayout!!.autoRefresh()
            } else {
                smartRefreshLayout!!.finishRefresh()
            }
        }
    }

    fun setOnRefreshListener(listener: SwipeRefreshLayout.OnRefreshListener?) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout!!.setOnRefreshListener(listener)
        } else if (smartRefreshLayout != null) {
            smartRefreshLayout!!.setOnRefreshListener {
                listener?.onRefresh()
            }
        }
    }

    fun setColorSchemeResources(@ColorRes vararg colorResIds: Int) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout!!.setColorSchemeResources(*colorResIds)
        } else if (smartRefreshLayout != null) {
            smartRefreshLayout!!.setPrimaryColorsId(*colorResIds)
        }
    }
}