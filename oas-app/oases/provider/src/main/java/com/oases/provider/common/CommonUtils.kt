package com.oases.provider.common

import com.alibaba.android.arouter.launcher.ARouter
import com.oases.base.common.BaseConstant
import com.oases.base.utils.AppPrefsUtils

/*
    顶级函数，判断是否登录
 */
fun isLogined():Boolean{
    return AppPrefsUtils.getString(BaseConstant.USER_TOKEN).isNotEmpty()
}

/*
    如果已经登录，进行传入的方法处理
    如果没有登录，进入登录界面
 */
fun afterLogin(method:()->Unit){
    if (isLogined()){
        method()
    }else{
        ARouter.getInstance().build(com.oases.provider.router.RouterPath.UserCenter.PATH_LOGIN).navigation()
    }
}

//sync points updated
fun isMyPointsValid():Boolean{
    return AppPrefsUtils.getBoolean(BaseConstant.MY_POINTS)
}

fun invalidMyPoints():Boolean{
    return AppPrefsUtils.getBoolean(BaseConstant.MY_POINTS)
}

fun validMyPoints():Boolean{
    return AppPrefsUtils.getBoolean(BaseConstant.MY_POINTS)
}
