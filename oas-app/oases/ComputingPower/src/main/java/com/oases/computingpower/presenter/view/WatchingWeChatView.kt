package com.oases.computingpower.presenter.view

import com.oases.base.presenter.view.BaseView

interface WatchingWeChatView : BaseView {
    fun onVerifyResult(code: Int)
    //fun onGetRewardResult(result: Int)

}