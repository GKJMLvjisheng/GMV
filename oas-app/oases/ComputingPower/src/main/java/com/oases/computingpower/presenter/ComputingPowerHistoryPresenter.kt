package com.oases.computingpower.presenter

import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.computingpower.data.protocol.*
import com.oases.computingpower.presenter.view.ComputingPowerHistoryView
import com.oases.computingpower.presenter.view.InviteFriendsInfoView
import com.oases.computingpower.presenter.view.WatchingWeChatView
import com.oases.computingpower.service.ComputingPowerService
import javax.inject.Inject


class ComputingPowerHistoryPresenter @Inject constructor() : BasePresenter<ComputingPowerHistoryView>() {
    @Inject
    lateinit var computingPowerService: ComputingPowerService

    fun inquirePowerDetail(pageNumber:Int,pageSize:Int) {
        val req = ComputingPowerDetailReq(pageNumber,pageSize)
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        computingPowerService.inquirePowerDetail(req).execute(object : BaseSubscriber<ComputingPowerDetailResp>(mView) {
            override fun onNext(t: ComputingPowerDetailResp) {
                mView.OnGetInquirePowerDetail(t)
            }
        }, lifecycleProvider)
    }
}