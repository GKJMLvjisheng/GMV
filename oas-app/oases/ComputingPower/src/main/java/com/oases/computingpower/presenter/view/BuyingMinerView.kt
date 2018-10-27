package com.oases.computingpower.presenter.view

import com.oases.base.presenter.view.BaseView
import com.oases.computingpower.data.protocol.MinerInfoResp

interface BuyingMinerView : BaseView {
    fun onGetMinerInfoResult(result: MinerInfoResp)
    fun onConfirmBuyMinerResult(result: Int)
    //fun onGetRewardResult(result: Int)

}