package com.oases.presenter.view

import com.oases.base.presenter.view.BaseView
import com.oases.computingpower.data.protocol.KYCVerifyStatusResp
import com.oases.data.protocol.BalanceDetailResp

import com.oases.data.protocol.InquireAddressResp
import com.oases.data.protocol.InquirePointsDetailResp

interface OnLineWalletView: BaseView {
    fun setBalance(balance: BalanceDetailResp)
    fun onInquireAddress(address: InquireAddressResp)
    fun onGetTransactionDeails(list: InquirePointsDetailResp)
    fun onGetTransactionMoreDeails(list: InquirePointsDetailResp)
    fun onGetTransactionOutDeails(list: InquirePointsDetailResp)
    fun onGetTransactionInDeails(list: InquirePointsDetailResp)
    fun onGetKYCVerifyStatus(result: KYCVerifyStatusResp)

}