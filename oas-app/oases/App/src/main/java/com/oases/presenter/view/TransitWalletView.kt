package com.oases.presenter.view

import com.oases.base.presenter.view.BaseView
import com.oases.data.protocol.ListCoinResp
import com.oases.data.protocol.TransitWalletSummary

interface TransitWalletView: BaseView{
    fun setCoin(coins: ListCoinResp)
    fun setSummary(summary: TransitWalletSummary)
}