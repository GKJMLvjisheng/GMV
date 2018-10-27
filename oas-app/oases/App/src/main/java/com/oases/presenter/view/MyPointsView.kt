package com.oases.presenter.view

import com.oases.base.presenter.view.BaseView
import com.oases.data.protocol.CurrentPeriodPointsResp
import com.oases.data.protocol.InquireEnergyDetailResp
import com.oases.data.protocol.InquirePointsDetailResp

interface MyPointsView: BaseView {
    fun onGetPoints(points: Float)
    fun onGetCurrentPeriodPoints(currentPeriodPointsResp: CurrentPeriodPointsResp)
    fun onGetPointsDeails(list:InquireEnergyDetailResp)
   /* fun onGetTransactionDeails(list: InquirePointsDetailResp)
    fun onGetTransactionOutDeails(list: InquirePointsDetailResp)
    fun onGetTransactionInDeails(list: InquirePointsDetailResp)*/
   fun onGetTransactionDeails(list: InquireEnergyDetailResp)
    fun onGetTransactionOutDeails(list: InquireEnergyDetailResp)
    fun onGetTransactionInDeails(list: InquireEnergyDetailResp)
}