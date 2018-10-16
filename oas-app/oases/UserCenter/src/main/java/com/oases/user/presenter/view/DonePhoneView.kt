package com.oases.user.presenter.view

import com.oases.base.presenter.view.BaseView
import com.oases.user.data.protocol.DonePhoneResp
import com.oases.user.data.protocol.UserInfo

interface DonePhoneView : BaseView{
    fun onDonePhoneResult(result: DonePhoneResp)
    fun onGetRewardResult(result: Int)
}