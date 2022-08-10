package com.cool.lib.page

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.ObjectUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.cool.lib.R
import com.cool.lib.bean.BaseResultPageBean

/**
 * 分页辅助工具类
 */
open class PageUtil<T>(
    context: Context?,
    pageSize: Int,
    refreshLayout: RefreshLayout?,
    recyclerView: RecyclerView,
    adapter: BaseQuickAdapter<T, out BaseViewHolder>,
    isEnableLoadMore: Boolean,
    emptyView: View?,
) {
    companion object {
        const val PAGE_SIZE = 20
        const val PAGE_NUM_FIRST = 1

        /**
         * 设置下拉进度的主题颜色
         *
         * @param refreshLayout refreshLayout
         */
        private fun setSrlColor(refreshLayout: RefreshLayout) {
            refreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary, R.color.teal_700)
        }

        private fun inflate(context: Context?, emptyResId: Int): View? {
            var view: View? = null
            try {
                if (emptyResId > 0) {
                    view = View.inflate(context, emptyResId, null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return view
        }
    }

    private var isLoadingData = false
    private var isEnableLoadMore = false
    var pageNum = 0
        private set
    open var pageSize = 0
    private var context: Context? = null
    private var refreshLayout: RefreshLayout? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: BaseQuickAdapter<T, out BaseViewHolder>? = null
    private var emptyView: View? = null
    private var listener: RequestLoadMoreListener<T>? = null
    var isUserRefreshThisTime = false
        private set

    constructor(
        context: Context?,
        refreshLayout: RefreshLayout?,
        recyclerView: RecyclerView,
        adapter: BaseQuickAdapter<T, out BaseViewHolder>,
    ) : this(context, PAGE_SIZE, refreshLayout, recyclerView, adapter) {
    }

    @JvmOverloads
    constructor(
        context: Context?,
        pageSize: Int,
        refreshLayout: RefreshLayout?,
        recyclerView: RecyclerView,
        adapter: BaseQuickAdapter<T, out BaseViewHolder>,
        isEnableLoadMore: Boolean = true,
        emptyResId: Int = R.layout.layout_base_list_empty,
    ) : this(context, pageSize, refreshLayout, recyclerView, adapter, isEnableLoadMore, inflate(context, emptyResId)) {
    }

    init {
        try {
            this.isEnableLoadMore = isEnableLoadMore
            this.context = context
            pageNum = PAGE_NUM_FIRST
            this.pageSize = pageSize
            this.refreshLayout = refreshLayout
            this.recyclerView = recyclerView
            this.adapter = adapter
            if (recyclerView.layoutManager == null) {
                recyclerView.layoutManager = LinearLayoutManager(context)
            }
            setRecyclerEmptyView(handleEmptyView(emptyView))
            adapter.recyclerView = recyclerView
            recyclerView.adapter = adapter
            val loadMoreModule = adapter.loadMoreModule
            loadMoreModule.isEnableLoadMore = isEnableLoadMore
            if (isEnableLoadMore) {
                adapter.animationEnable = true
                loadMoreModule.preLoadNumber = pageSize
                loadMoreModule.setOnLoadMoreListener { doLoadMore() }
            }
            if (refreshLayout != null) {
                setSrlColor(refreshLayout)
                refreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
                    doRefresh(true)
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleEmptyView(emptyView: View?): View? {
        if (emptyView != null) {
            val textView = emptyView.findViewById<TextView>(R.id.state_describe)
            if (textView != null) {
                textView.text = "暂无数据"
            }
        }
        return emptyView
    }

    fun setListener(listener: RequestLoadMoreListener<T>?) {
        this.listener = listener
    }

    fun setRecyclerEmptyView(emptyResId: Int) {
        setRecyclerEmptyView(inflate(context, emptyResId))
    }

    fun setRecyclerEmptyView(emptyView: View?) {
        this.emptyView = emptyView
    }

    fun doRefresh() {
        doRefresh(false)
    }

    private fun doRefresh(userRefreshThisTime: Boolean) {
        isUserRefreshThisTime = userRefreshThisTime
        if (!isLoadingData) {
            isLoadingData = true
            refresh()
            if (listener != null) {
                listener!!.onLoadData(pageNum, pageSize)
            }
        }
    }

    private fun doLoadMore() {
        if (!isLoadingData) {
            isLoadingData = true
            loadMore()
            if (listener != null) {
                listener!!.onLoadData(pageNum, pageSize)
            }
        }
    }

    private fun refresh() {
        pageNum = PAGE_NUM_FIRST
    }

    private fun loadMore() {
        if (isEnableLoadMore) {
            pageNum++
        } else {
            refresh()
        }
    }

    fun handleDataFailed() {
        handleData(0, null)
    }

    fun handleData(count: Int, data: MutableList<T>?) {
        handleData(BaseResultPageBean(count), data)
    }

    fun handleData(baseResultPageBean: BaseResultPageBean<T>?, data: MutableList<T>?) {
        try {
            val loadMoreModule = adapter!!.loadMoreModule
            if (baseResultPageBean != null && data != null) {
                if (listener != null) {
                    setOrAddData(listener!!.handleData(pageNum, pageSize, data))
                } else {
                    setOrAddData(data)
                }
                if (isEnableLoadMore) {
                    if (baseResultPageBean.count <= 0) {
                        loadMoreModule.loadMoreEnd()
                    } else if (baseResultPageBean.count <= pageNum * pageSize) {
                        loadMoreModule.loadMoreEnd()
                    } else {
                        loadMoreModule.loadMoreComplete()
                    }
                }
            } else {
                loadMoreFail()
            }
        } catch (e: Exception) {
            loadMoreFail()
            e.printStackTrace()
        } finally {
            setLoadingData(false)
            checkEmptyView()
        }
    }

    private fun setOrAddData(data: MutableList<T>?) {
        if (pageNum == PAGE_NUM_FIRST) {
            adapter!!.setNewInstance(data)
        } else {
            if (ObjectUtils.isNotEmpty(data)) {
                adapter!!.addData(data!!)
            }
        }
    }

    private fun checkEmptyView() {
        if (emptyView != null) {
            adapter!!.setEmptyView(emptyView!!)
            emptyView = null
        }
    }

    private fun loadMoreFail() {
        if (listener != null) {
            setOrAddData(listener!!.handleData(pageNum, pageSize, null))
        }
        if (isEnableLoadMore) {
            adapter!!.loadMoreModule.loadMoreFail()
        }
        pageNum--
        if (pageNum < 1) {
            pageNum = 1
        }
    }

    fun isLoadingData(): Boolean {
        return isLoadingData
    }

    fun setLoadingData(loadingData: Boolean) {
        isLoadingData = loadingData
        if (!isLoadingData) {
            refreshLayout?.setRefreshing(false)
        }
    }
}