@file:JvmName(RECYCLER_VIEW_EXTEND)

package com.cool.lib.ext

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 *
 * @author yfc
 * @since 2022/07/29 09:13
 * @version V1.0
 */
fun <T> RecyclerView.simplyAdapterBVH(
    @LayoutRes layoutResId: Int,
    data: MutableList<T>? = null,
    layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context),
    convert: (BaseViewHolder, T) -> Unit,
): BaseQuickAdapter<T, BaseViewHolder> {
    return simplyAdapter(layoutResId, data, layoutManager, convert)
}

fun <T, BD : ViewDataBinding> RecyclerView.simplyAdapterBDBH(
    @LayoutRes layoutResId: Int,
    data: MutableList<T>? = null,
    layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context),
    convert: (BaseDataBindingHolder<BD>, T) -> Unit,
): BaseQuickAdapter<T, BaseDataBindingHolder<BD>> {
    return simplyAdapter(layoutResId, data, layoutManager, convert)
}

private inline fun <T, reified VH : BaseViewHolder> RecyclerView.simplyAdapter(
    @LayoutRes layoutResId: Int = 0,
    data: MutableList<T>? = null,
    layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context),
    crossinline convert: (VH, T) -> Unit,
): BaseQuickAdapter<T, VH> {
    this.layoutManager = layoutManager
    val adapter = object : BaseQuickAdapter<T, VH>(layoutResId, data) {
        override fun convert(holder: VH, item: T) {
            convert.invoke(holder, item)
        }
    }
    this.adapter = adapter
    return adapter
}