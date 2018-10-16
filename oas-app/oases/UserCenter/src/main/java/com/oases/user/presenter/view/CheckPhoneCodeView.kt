package com.oases.user.presenter.view

import com.oases.base.presenter.view.BaseView
import com.oases.user.data.protocol.CheckPhoneCodeResp
import com.oases.user.data.protocol.SendMobileResp

interface CheckPhoneCodeView : BaseView{
    fun onCheckPhoneCodeResult(result: CheckPhoneCodeResp)
    fun onSendMobileResult(result: SendMobileResp)
}