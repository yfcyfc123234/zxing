package com.cool.lib.util

import java.util.regex.Pattern

object PatternUtils {
    /**
     * @param pwd pwd
     * @return 判断密码是否符合规则
     * 8位以上数字+字母
     */
    fun isPwdUseable(pwd: String): Boolean {
        return isEasyPwd(pwd) && containsLetter(pwd) && containsNumber(pwd)
    }

    /**
     * @param houseNum houseNum
     * @return 判断是否无规则房源编号  15位以内数字+字母
     */
    fun isOutRuleHouseNum(houseNum: String): Boolean {
        return isEasyHouseNum(houseNum) && containsNumber(houseNum) && containsLetter(houseNum)
    }

    /**
     * @param input input
     * @return 判断是否包含数字
     */
    fun containsNumber(input: String): Boolean {
        val p = Pattern.compile("[0-9]+")
        val m = p.matcher(input)
        return m.find()
    }

    /**
     * @param input input
     * @return 判断是否包含字母
     */
    fun containsLetter(input: String): Boolean {
        val p = Pattern.compile("[a-zA-Z]+")
        val m = p.matcher(input)
        return m.find()
    }

    /**
     * @param input input
     * @return 确认是否全是数字和字母组成和位数是否符合要求，但无法确定是否为数字和字母混合
     */
    fun isEasyPwd(input: String): Boolean {
        return Pattern.matches("[0-9a-zA-Z]{8,}", input)
    }

    /**
     * @param input input
     * @return 判断是否为手机号
     */
    fun isPhoneNumber(input: String): Boolean {
        return Pattern.matches("1[0-9]{10}", input)
    }

    /**
     * @param input input
     * @return 确认是否全是数字和字母组成和位数是否符合要求，但无法确定是否为数字和字母混合
     */
    fun isEasyHouseNum(input: String): Boolean {
        return Pattern.matches("[0-9a-zA-Z]{1,15}", input)
    }

    /**
     * @param input input
     * @param mode  1四位数字+横杠 2四位数字  3无规则
     * @return 验证门牌号
     */
    fun isDoorNumber(input: String, mode: Int): Boolean {
        if (mode == 1) {
            return Pattern.matches("[0-9]{2}-[0-9]{2}", input)
        }
        if (mode == 2) {
            return Pattern.matches("[0-9]{4}", input)
        }
        return if (mode == 3) {
            input.isNotEmpty()
        } else false
    }

    /**
     * @param email email
     * @return 验证邮箱 数字或者字母@数字或字母.数字或字母
     */
    fun isEmail(email: String?): Boolean {
        if (null == email || "" == email) return false
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        val p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*") //复杂匹配
        val m = p.matcher(email)
        return m.matches()
    }

    /**
     * **正则表达式:验证身份证**
     */
    const val REGEX_ID_CARD = "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])"

    /**
     * 校验身份证
     *
     * @param idCard idCard
     * @return 校验通过返回true，否则返回false
     */
    fun isIDCard(idCard: String): Boolean {
        return Pattern.matches(REGEX_ID_CARD, idCard)
    }
}