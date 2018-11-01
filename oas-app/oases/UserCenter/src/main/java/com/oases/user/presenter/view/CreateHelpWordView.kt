package com.oases.user.presenter.view

import com.oases.base.presenter.view.BaseView
import com.oases.user.data.protocol.confirmOldPwdResp

interface CreateHelpWordView : BaseView{
    fun onConfirmOldPwdResult(result: confirmOldPwdResp)
}