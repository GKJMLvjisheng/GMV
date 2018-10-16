package com.oases.computingpower.presenter

import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.computingpower.data.protocol.InviteFriendsInfoResp
import com.oases.computingpower.data.protocol.WatchingWeChatCodeVerifyReq
import com.oases.computingpower.data.protocol.WatchingWeChatCodeVerifyResp
import com.oases.computingpower.presenter.view.InviteFriendsInfoView
import com.oases.computingpower.presenter.view.WatchingWeChatView
import com.oases.computingpower.service.ComputingPowerService
import javax.inject.Inject


class InviteFriendsInfoPresenter @Inject constructor() : BasePresenter<InviteFriendsInfoView>() {
    @Inject
    lateinit var computingPowerService: ComputingPowerService

    fun getInviteFriendsInfo() {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        computingPowerService.getInviteFriendsInfo().execute(object : BaseSubscriber<InviteFriendsInfoResp>(mView) {
            override fun onNext(t: InviteFriendsInfoResp) {
                mView.onInviteFriendsInfoResult(t)
            }
        }, lifecycleProvider)
    }
}