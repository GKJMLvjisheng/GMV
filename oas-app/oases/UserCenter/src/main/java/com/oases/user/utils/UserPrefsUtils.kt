package com.oases.user.utils

import com.oases.base.common.BaseConstant
import com.oases.base.utils.AppPrefsUtils
import com.oases.user.data.protocol.UserInfo

fun putUserInfo(userInfo: UserInfo?) {
    AppPrefsUtils.putString(BaseConstant.USER_ID, userInfo?.userId ?: "")
    AppPrefsUtils.putString(BaseConstant.USER_NAME, userInfo?.name ?: "")
    AppPrefsUtils.putString(BaseConstant.USER_ICON, userInfo?.profile ?: "")
    AppPrefsUtils.putString(BaseConstant.USER_NICK_NAME, userInfo?.nickname ?: "")
    AppPrefsUtils.putString(BaseConstant.USER_GENDER, userInfo?.gender ?: "")
    AppPrefsUtils.putString(BaseConstant.USER_ADDRESS, userInfo?.address ?: "")
    AppPrefsUtils.putString(BaseConstant.USER_BIRTHDAY, userInfo?.birthday ?: "")
    AppPrefsUtils.putString(BaseConstant.USER_PHONE_NUMBER, userInfo?.mobile ?: "")
    AppPrefsUtils.putString(BaseConstant.USER_MAIL_ADDRESS, userInfo?.email ?: "")
    AppPrefsUtils.putString(BaseConstant.USER_INVITE_CODE, userInfo?.inviteCode ?: "")

}