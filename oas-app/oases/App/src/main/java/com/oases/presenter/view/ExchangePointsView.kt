package com.oases.presenter.view

import com.oases.base.presenter.view.BaseView
import com.oases.data.protocol.InquirePointFactorResp

interface ExchangePointsView: BaseView {
    fun onGetFactor(factor: InquirePointFactorResp)
    fun onRedeemSucceed()
    fun onRedeemFailed()
}