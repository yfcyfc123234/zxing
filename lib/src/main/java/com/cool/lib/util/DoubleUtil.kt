package com.cool.lib.util

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

object DoubleUtil {
    /**
     * 格式化为0.00
     */
    fun formatMoney(number: Double): String {
        val decimalFormat = DecimalFormat("#0.00")
        return decimalFormat.format(number)
    }

    /**
     * 保留两位小数，只舍不入
     */
    fun formatNumbers(number: String?): Double {
        val bigDecimal = BigDecimal(number)
        return bigDecimal.setScale(2, RoundingMode.DOWN).toString().toDouble()
    }

    fun add(d1: Double, d2: Double): Double {
        return add(d1.toString(), d2.toString())
    }

    /**
     * 精确加法
     */
    fun add(d1: String?, d2: String?): Double {
        val b1 = BigDecimal(d1)
        val b2 = BigDecimal(d2)
        return b1.add(b2).toDouble()
    }

    fun subtract(d1: Double, d2: Double): Double {
        return subtract(d1.toString(), d2.toString())
    }

    /**
     * 精确减法
     */
    fun subtract(d1: String?, d2: String?): Double {
        val b1 = BigDecimal(d1)
        val b2 = BigDecimal(d2)
        return b1.subtract(b2).toDouble()
    }

    fun multiply(d1: Double, d2: Double): Double {
        return multiply(d1.toString(), d2.toString())
    }

    /**
     * 精确乘法
     *
     * @param v1 v1
     * @param v2 v2
     * @return double
     */
    fun multiply(v1: String?, v2: String?): Double {
        val b1 = BigDecimal(v1)
        val b2 = BigDecimal(v2)
        return b1.multiply(b2).toDouble()
    }

    /**
     * 提供（相对）精确的除法运算。 当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
     *
     * @param dividend 被除数
     * @param divisor  除数
     * @param scale    表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    fun divide(dividend: Double?, divisor: Double?, scale: Int, round: Int?): Double {
        require(scale >= 0) { "The scale must be a positive integer or zero" }
        val b1 = BigDecimal(java.lang.Double.toString(dividend!!))
        val b2 = BigDecimal(java.lang.Double.toString(divisor!!))
        return b1.divide(b2, scale, round!!).toDouble()
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param value 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    fun roundHalfUp(value: Double?, scale: Int): Double {
        require(scale >= 0) { "The scale must be a positive integer or zero" }
        val b = BigDecimal(java.lang.Double.toString(value!!))
        val one = BigDecimal("1")
        return b.divide(one, scale, RoundingMode.HALF_UP).toDouble()
    }

    fun roundDown(value: Double?, scale: Int): Double {
        require(scale >= 0) { "The scale must be a positive integer or zero" }
        val b = BigDecimal(java.lang.Double.toString(value!!))
        val one = BigDecimal("1")
        return b.divide(one, scale, RoundingMode.DOWN).toDouble()
    }

    fun compareBigDecimal(amount: Double, compare: Double): Boolean {
        return compareBigDecimal(amount.toString(), compare.toString())
    }

    /**
     * 比较大小
     *
     * @param amount  输入的数值
     * @param compare 被比较的数字
     * @return true 大于等于被比较的数
     */
    fun compareBigDecimal(amount: String?, compare: String?): Boolean {
        val bd1 = BigDecimal(amount)
        val bd2 = BigDecimal(compare)
        return bd1.compareTo(bd2) != -1
    }

    fun getPriceStr(price: Double): String {
        return "¥" + getPriceStrWithoutUnit(price)
    }

    fun getPriceStrYuan(price: Double): String {
        return getPriceStrWithoutUnit(price) + "元"
    }

    fun getPriceStrYuan4(price: Double): String {
        return getPriceStrWithoutUnit(price, 4) + "元"
    }

    fun getPriceStrWithoutUnit(price: Double): String {
        return String.format(Locale.getDefault(), "%.2f", getPrice(price, 2))
    }

    fun getPriceStrWithoutUnit(price: Double, scale: Int): String {
        return String.format(Locale.getDefault(), "%." + scale + "f", getPrice(price, scale))
    }

    fun getPrice(price: Double, scale: Int): Double {
        return roundDown(price, scale)
    }
}