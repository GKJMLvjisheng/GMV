package com.oases.presenter.view

import com.oases.base.presenter.view.BaseView
import com.oases.data.protocol.InquireEnergyDetailResp
import com.oases.data.protocol.InquirePointsDetailResp
import com.oases.data.protocol.ListCoinResp
import com.oases.data.protocol.PointItem

interface ExchangeDetailView: BaseView{
    fun onGetTransactionMoreDeails(list: InquirePointsDetailResp)
    fun onGetTransactionOutDeails(list: InquirePointsDetailResp)
    fun onGetTransactionInDeails(list: InquirePointsDetailResp)
    fun onGetEnergyMoreDeails(list: InquireEnergyDetailResp)
    fun onGetOasAllDeails(list: InquirePointsDetailResp)
    fun setCoin(coins: ListCoinResp)
    fun getExchangeResult(t:Int)
}