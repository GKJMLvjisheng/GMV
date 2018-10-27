package com.oases.presenter.view

import com.oases.base.presenter.view.BaseView
import com.oases.computingpower.data.protocol.KYCVerifyStatusResp
import java.math.BigDecimal

interface WalletOutView: BaseView{
    fun onGetWalletOutEvent(value:Int)
    fun onGetDayMoneyTotal(result: BigDecimal)
    fun onGetKYCVerifyStatus(result: KYCVerifyStatusResp)

}