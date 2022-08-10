@file:JvmName(BANNER_EXTEND)

package com.cool.lib.ext

import android.content.Context
import androidx.annotation.IntRange
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.cool.lib.banner.BaseBannerAdapter
import com.cool.lib.banner.BaseBannerBean
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.indicator.Indicator
import com.youth.banner.indicator.RectangleIndicator
import com.youth.banner.indicator.RoundLinesIndicator
import com.youth.banner.transformer.*
import kotlin.random.Random


/**
 *
 * @author yfc
 * @since 2022/07/29 09:14
 * @version V1.0
 */
fun <T, VH : RecyclerView.ViewHolder, BA : BannerAdapter<out T, out VH>> Banner<out T, out BA>.setBanner(
    list: List<BaseBannerBean>?,
    needExtra: Boolean = false,
): Banner<T, BA> {
    val indicator = getBannerIndicator(context, Random.nextInt(1, 4))
    val transformer = getBannerPageTransformer(Random.nextInt(1, 10))
    val extra: String? = if (needExtra) {
        "indicator->" + indicator.javaClass.simpleName +
                "\ntransformer->" + transformer.javaClass.simpleName
    } else {
        null
    }
    return addBannerLifecycleObserver(context as? LifecycleOwner)
        .setIndicator(indicator)
        .addPageTransformer(transformer)
        .setAdapter(BaseBannerAdapter(list, extra = extra)) as Banner<T, BA>
}

fun getBannerIndicator(context: Context, @IntRange(from = 1, to = 3) type: Int = 1): Indicator {
    return when (type) {
        2 -> {
            CircleIndicator(context)
        }
        3 -> {
            RoundLinesIndicator(context)
        }
        else -> {
            RectangleIndicator(context)
        }
    }
}

fun getBannerPageTransformer(@IntRange(from = 1, to = 9) type: Int = 1): ViewPager2.PageTransformer {
    return when (type) {
        2 -> {
            AlphaPageTransformer()
        }
        3 -> {
            DepthPageTransformer()
        }
        4 -> {
            MZScaleInTransformer()
        }
        5 -> {
            RotateDownPageTransformer()
        }
        6 -> {
            RotateUpPageTransformer()
        }
        7 -> {
            RotateYTransformer()
        }
        8 -> {
            ScaleInTransformer()
        }
        9 -> {
            ZoomOutPageTransformer()
        }
        else -> {
            MZScaleInTransformer()
        }
    }
}