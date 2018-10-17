package com.oases.computingpower.presenter.view

import com.oases.base.presenter.view.BaseView
import com.oases.computingpower.data.protocol.ComputingPowerDetailResp

interface ComputingPowerHistoryView : BaseView {
    fun OnGetInquirePowerDetail(result: ComputingPowerDetailResp)
}