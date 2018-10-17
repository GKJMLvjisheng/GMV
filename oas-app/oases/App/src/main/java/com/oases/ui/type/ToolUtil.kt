package com.oases.ui.type

import java.math.BigDecimal
import java.util.regex.Pattern

/**
 * 工具类
 */
object ToolUtil {

    //利用正则表达式判断,满足为true
    fun regularExpressionValidate(value:String,exp:String):Boolean{
        val pattern: Pattern = Pattern.compile(exp)
        return pattern.matcher(value.toString()).matches()
    }

    //转出-计算ETH,转成9位小数
    fun caculateETH(value:Int,actor:Int):String{
        return (value*actor*Math.pow(10.0,-9.0)).toBigDecimal().setScale(9, BigDecimal.ROUND_DOWN).toString()
    }

    //转出-地址格式转换
    fun modifyAddressInfo(str:String):String{
        var result :String= ""
        if(str.length>0){
            for(i in 0 until str.length step 4){
                result += str.substring(i,i+4).plus(" ")
            }
        }
        return result
    }
}