package com.cool.lib.banner

import com.cool.lib.bean.BaseBean
import java.io.Serializable

open class BaseBannerBean(
    var any: Any? = null,
    var cornerRadius: Float = 0F,
    var type: BannerType = BannerType.IMAGE,
    var autoCheckBannerType: Boolean = true,
    var extra: Serializable? = null,
) : BaseBean(), BaseBannerInterface {
    init {
        if (autoCheckBannerType) {
            if (any is String) {
                val anyData = any as String
                if (anyData.endsWith(".json", true)) {
                    type = BannerType.LOTTIE
                }
            }
        }
    }

    override fun getData(): Any? = any
}

enum class BannerType {
    IMAGE,
    LOTTIE,
}
