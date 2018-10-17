package com.oases.user.presenter.view

import com.oases.base.presenter.view.BaseView
import com.oases.user.data.protocol.SendMailResp
import com.oases.user.data.protocol.SendMobileResp

interface MailStepOneView : BaseView{
    fun onCheckMailResult(result: Int)
    //fun onSendMailResult(result: SendMailResp)
}