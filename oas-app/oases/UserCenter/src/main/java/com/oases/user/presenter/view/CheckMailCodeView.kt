package com.oases.user.presenter.view

import com.oases.base.presenter.view.BaseView
import com.oases.user.data.protocol.CheckMailCodeResp
import com.oases.user.data.protocol.CheckPhoneCodeResp
import com.oases.user.data.protocol.SendMailResp

interface CheckMailCodeView : BaseView{
    fun onCheckMailCodeResult(result: CheckMailCodeResp)
    fun onSendMailResult(result: SendMailResp)
}