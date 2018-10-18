package com.oases.computingpower.presenter.view

import com.oases.base.presenter.view.BaseView
import com.oases.computingpower.data.protocol.BuyingMinerHistoryResp
import com.oases.computingpower.data.protocol.MinerInfoResp

interface BuyingMinerHistoryView : BaseView {
    fun onGetBuyingMinerHistoryResult(result: BuyingMinerHistoryResp)

}