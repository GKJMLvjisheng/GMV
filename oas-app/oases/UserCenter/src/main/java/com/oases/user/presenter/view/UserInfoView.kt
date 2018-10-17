package com.oases.user.presenter.view

import android.media.MediaRouter
import com.oases.base.presenter.view.BaseView
import com.oases.user.data.protocol.UserInfo
import com.oases.user.data.protocol.uploadImgResp

interface UserInfoView : BaseView{
    fun onUpdateUserIconResult(result: uploadImgResp)
    fun onUpdateBirthdayResult(result: UserInfo)
    fun onUpdateGenderResult(result: UserInfo)
}