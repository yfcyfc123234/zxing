package com.cool.lib.constant

/**
 * @author yfc
 * @version V1.0
 * @since 2022/06/23 10:59
 * 一些常用的常量定义
 */
interface CommonConstant {
    companion object {
        const val SUCCESS = 564213
        const val FAILED = SUCCESS + 1
        const val ERROR = SUCCESS + 2
        const val PROCESSING = SUCCESS + 3

        const val ZERO = 0
        const val ONE = 1
        const val TWO = 2
        const val THREE = 3
        const val FOUR = 4
        const val FIVE = 5
        const val SIX = 6
        const val SEVEN = 7
        const val EIGHT = 8
        const val NINE = 9
        const val TEN = 10

        const val FIFTY = 50
        const val NINETY_NINE = 99
        const val ONE_HUNDRED = 100
        const val TWO_HUNDRED = 200
        const val FOUR_HUNDRED = 400
        const val FIVE_HUNDRED = 500
        const val EIGHT_HUNDRED = 800
        const val ONE_THOUSAND = 1000

        const val HEX_0XFF = 0XFF //255
        const val HEX_0X400 = 0X400 //1024

        const val ELLIPSIS_EN = "......"
        const val ELLIPSIS_CH = "……"
    }
}