package com.oases.user.presenter.view

import com.oases.base.presenter.view.BaseView
import com.oases.user.data.protocol.SendMobileResp

interface PhoneStepOneView : BaseView{
    fun onCheckPhoneResult(result: Int)
   // fun onSendMobileResult(result: SendMobileResp)
}