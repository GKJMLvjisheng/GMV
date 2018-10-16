package com.oases.computingpower.presenter.view

import com.oases.base.presenter.view.BaseView
import com.oases.computingpower.data.protocol.KYCInfoResp
import com.oases.computingpower.data.protocol.KYCVerifyStatusResp

interface KYCView : BaseView {
    fun onUpLoadKYCInfoResult(result: KYCInfoResp)
    fun onGetKYCVerifyStatus(result: KYCVerifyStatusResp)
    fun onChangeKYCStatus(result: Int)
}