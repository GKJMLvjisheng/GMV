package com.oases.computingpower.presenter.view

import com.oases.base.presenter.view.BaseView
import com.oases.computingpower.data.protocol.MailStatusResp
import com.oases.computingpower.data.protocol.PhoneStatusResp
import com.oases.computingpower.data.protocol.PowerActivityStatusResp

interface ComputingPowerMainView: BaseView {
    fun onGetPower(power: Int)
    fun onCheckPhoneResult(result:PhoneStatusResp)
    fun onCheckMailResult(result: MailStatusResp)
    fun onInquirePowerActivityStatusResult(result: PowerActivityStatusResp)


}