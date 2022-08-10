package com.cool.lib.util

class NumberCastUtil {
    fun getD(d: Int): String {
//       String[] str = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
        val str = arrayOf("零", "一", "二", "三", "四", "五", "六", "七", "八", "九")
        //       String ss[] = new String[] { "元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿" };
        val ss = arrayOf("", "十", "百", "千", "万", "十", "百", "千", "亿")
        val s = d.toString()
        var sb = StringBuffer()
        for (element in s) {
            val index = element.toString()
            sb = sb.append(str[index.toInt()])
        }
        val sss = sb.toString()
        var i = 0
        for (j in sss.length downTo 1) {
            sb = sb.insert(j, ss[i++])
        }
        //        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
//        if ( pattern.matcher(s).matches()){
//            res=res.replace("零","");
//        }
        return sb.toString().trim { it <= ' ' }
    }
}