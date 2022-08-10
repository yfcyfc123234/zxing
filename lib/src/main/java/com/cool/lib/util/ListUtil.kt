package com.cool.lib.util

import com.blankj.utilcode.util.ReflectUtils
import com.cool.lib.ext.logE
import java.text.NumberFormat
import java.util.*
import kotlin.math.max

/**
 * 在数据库中查出来的列表中，往往需要对不同的字段重新排序。 一般的做法都是使用排序的字段，重新到数据库中查询。
 * 如果不到数据库查询，直接在第一次查出来的list中排序，无疑会提高系统的性能。 下面就写一个通用的方法，对list排序，
 *
 *
 * 至少需要满足以下5点：
 *
 *
 * ①.list元素对象类型任意
 * ---->使用泛型解决
 *
 *
 * ②.可以按照list元素对象的任意多个属性进行排序,即可以同时指定多个属性进行排序
 * --->使用java的可变参数解决
 *
 *
 * ③.list元素对象属性的类型可以是数字(byte、short、int、long、float、double等，包括正数、负数、0)、字符串(char、String)、日期(java.util.Date)
 * --->对于数字：统一转换为固定长度的字符串解决,比如数字3和123，转换为"003"和"123" ;再比如"-15"和"7"转换为"-015"和"007"
 * --->对于日期：可以先把日期转化为long类型的数字，数字的解决方法如上
 *
 *
 * ④.list元素对象的属性可以没有相应的getter和setter方法
 * --->可以使用java反射进行获取private和protected修饰的属性值
 *
 *
 * ⑤.list元素对象的对象的每个属性都可以指定是升序还是降序
 * -->使用2个重写的方法(一个方法满足所有属性都按照升序(降序)，另外一个方法满足每个属性都能指定是升序(降序))
 */
object ListUtil {
    /**
     * 对list的元素按照多个属性名称排序,
     * list元素的属性可以是数字（byte、short、int、long、float、double等，支持正数、负数、0）、char、String、java.util.Date
     *
     * @param list        list
     * @param isAsc       true升序，false降序
     * @param sortNameArr list元素的属性名称
     */
    fun <E : Any> sort(list: MutableList<E>?, isAsc: Boolean, vararg sortNameArr: String) {
        list?.sortWith { a: E, b: E ->
            var ret = 0
            kotlin.runCatching {
                for (sortName in sortNameArr) {
                    ret = compareObject(sortName, isAsc, a, b)
                    if (0 != ret) {
                        break
                    }
                }
            }.onFailure {
                logE(it)
            }
            ret
        }
    }

    /**
     * 给list的每个属性都指定是升序还是降序
     *
     * @param list        list
     * @param sortNameArr 参数数组
     * @param typeArr     每个属性对应的升降序数组， true升序，false降序
     */
    fun <E : Any> sort(list: MutableList<E>?, sortNameArr: Array<String>, typeArr: BooleanArray) {
        if (sortNameArr.size != typeArr.size) {
            throw RuntimeException("属性数组元素个数和升降序数组元素个数不相等")
        }
        list?.sortWith { a: E, b: E ->
            var ret = 0
            kotlin.runCatching {
                for (i in sortNameArr.indices) {
                    ret = compareObject(sortNameArr[i], typeArr[i], a, b)
                    if (0 != ret) {
                        break
                    }
                }
            }.onFailure {
                logE(it)
            }
            ret
        }
    }

    /**
     * 对2个对象按照指定属性名称进行排序
     *
     * @param sortName 属性名称
     * @param isAsc    true升序，false降序
     * @param a        a
     * @param b        b
     * @return int
     * @throws Exception Exception
     */
    private fun <E : Any> compareObject(sortName: String, isAsc: Boolean, a: E, b: E): Int {
        val ret: Int
        val value1 = forceGetFieldValue(a, sortName)
        val value2 = forceGetFieldValue(b, sortName)
        var str1 = value1.toString()
        var str2 = value2.toString()
        if (value1 is Number && value2 is Number) {
            val maxLen = max(str1.length, str2.length)
            str1 = addZero2Str(value1, maxLen)
            str2 = addZero2Str(value2, maxLen)
        } else if (value1 is Date && value2 is Date) {
            val time1 = value1.time
            val time2 = value2.time
            val maxLen = max(time1, time2).toString().length
            str1 = addZero2Str(time1, maxLen)
            str2 = addZero2Str(time2, maxLen)
        }
        ret = if (isAsc) {
            str1.compareTo(str2)
        } else {
            str2.compareTo(str1)
        }
        return ret
    }

    /**
     * 给数字对象按照指定长度在左侧补0.
     *
     *
     * 使用案例: addZero2Str(11,4) 返回 "0011", addZero2Str(-18,6)返回 "-000018"
     *
     * @param number 数字对象
     * @param length 指定的长度
     * @return String
     */
    private fun addZero2Str(number: Number?, length: Int): String {
        val numberFormat = NumberFormat.getInstance()
        // 设置是否使用分组
        numberFormat.isGroupingUsed = false
        // 设置最大整数位数
        numberFormat.maximumIntegerDigits = length
        // 设置最小整数位数
        numberFormat.minimumIntegerDigits = length
        return numberFormat.format(number)
    }

    /**
     * 获取指定对象的指定属性值（去除private,protected的限制）
     *
     * @param obj       属性名称所在的对象
     * @param fieldName 属性名称
     * @return Object
     * @throws Exception Exception
     */
    private fun forceGetFieldValue(obj: Any, fieldName: String?): Any {
        return ReflectUtils.reflect(obj).field(fieldName).get()
    }
}