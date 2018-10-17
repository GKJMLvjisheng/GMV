package com.oases.user.presenter.view

import com.oases.base.presenter.view.BaseView
import com.oases.user.data.protocol.ForgetPwdOneResp
import com.oases.user.data.protocol.SendMobileResp

interface ForgetPwdOneView : BaseView{
    fun onInquireUserNameByMobileResult(result: ForgetPwdOneResp)
   // fun onSendMobileResult(result: SendMobileResp)
}