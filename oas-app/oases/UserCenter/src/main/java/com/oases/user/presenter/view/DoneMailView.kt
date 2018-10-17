package com.oases.user.presenter.view

import com.oases.base.presenter.view.BaseView
import com.oases.user.data.protocol.DoneMailResp
import com.oases.user.data.protocol.DonePhoneResp
import com.oases.user.data.protocol.UserInfo

interface DoneMailView : BaseView{
    fun onDoneMailResult(result: DoneMailResp)
    fun onGetRewardResult(result: Int)
}