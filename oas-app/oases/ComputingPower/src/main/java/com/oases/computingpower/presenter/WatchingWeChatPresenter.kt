package com.oases.computingpower.presenter

import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.computingpower.data.protocol.WatchingWeChatCodeVerifyReq
import com.oases.computingpower.data.protocol.WatchingWeChatCodeVerifyResp
import com.oases.computingpower.presenter.view.WatchingWeChatView
import com.oases.computingpower.service.ComputingPowerService
import javax.inject.Inject


class WatchingWeChatPresenter @Inject constructor() : BasePresenter<WatchingWeChatView>() {
    @Inject
    lateinit var computingPowerService: ComputingPowerService

    fun verifyWeChat(code: String) {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        computingPowerService.verifyWeChat(WatchingWeChatCodeVerifyReq(code)).execute(object : BaseSubscriber<WatchingWeChatCodeVerifyResp>(mView) {
            override fun onNext(t: WatchingWeChatCodeVerifyResp) {
                mView.onVerifyResult(t)
            }
        }, lifecycleProvider)
    }

    /*fun getReward(sourceCode: Int) {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        computingPowerService.getReward(sourceCode).execute(object : BaseSubscriber<Int>(mView) {
            override fun onNext(t: Int) {
                mView.onGetRewardResult(t)
            }
        }, lifecycleProvider)
    }*/
}