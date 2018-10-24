package com.oases.user.presenter.view

import com.oases.base.presenter.view.BaseView
import com.oases.user.data.protocol.confirmOldPwdResp

interface SetPasswordView : BaseView{
    fun onConfirmOldPwdResult(result: confirmOldPwdResp)
    fun onResetPwdResult(result: Int)
}