package com.oases.presenter.view

import com.oases.base.presenter.view.BaseView
import com.oases.data.protocol.AppUpdateResp
import com.oases.data.protocol.WalkPoint.InquireWalkPointResp

interface HomeView: BaseView {
    fun onInquireWalkPoint(value: InquireWalkPointResp)
}