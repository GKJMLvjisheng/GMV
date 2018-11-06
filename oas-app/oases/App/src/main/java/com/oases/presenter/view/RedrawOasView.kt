package com.oases.presenter.view

import com.oases.base.presenter.view.BaseView
import com.oases.data.protocol.OasResp

interface RedrawOasView: BaseView{
    //fun onRedrawResult()
    fun withdraw(t:Int)
    fun reverseWithdraw(t:Int)
    fun getOasExtra(t: OasResp)
}