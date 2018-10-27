package com.oases.computingpower.presenter.view

import com.oases.base.presenter.view.BaseView
import com.oases.computingpower.data.protocol.MailStatusResp
import com.oases.computingpower.data.protocol.PhoneStatusResp
import com.oases.computingpower.data.protocol.PowerActivityStatusResp
import java.math.BigDecimal

interface ComputingPowerMainView: BaseView {
    fun onGetPower(power: BigDecimal)
    fun onCheckPhoneResult(result:PhoneStatusResp)
    fun onCheckMailResult(result: MailStatusResp)
    fun onInquirePowerActivityStatusResult(result: PowerActivityStatusResp)


}