package com.oases.presenter.view

import com.oases.base.presenter.view.BaseView
import com.oases.data.protocol.ListCoinResp
import com.oases.data.protocol.TransferOasResp
import com.oases.data.protocol.TransitWalletSummary

interface ExchangeOutView: BaseView{
    fun onGetExchangeOutEvent(value: TransferOasResp)
}