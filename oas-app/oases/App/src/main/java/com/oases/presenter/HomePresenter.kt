package com.oases.presenter

import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.data.protocol.AppUpdateResp
import com.oases.data.protocol.WalkPoint.InquireWalkPointResp
import com.oases.presenter.view.HomeView
import com.oases.presenter.view.MainView
import com.oases.service.WalkPointService
import com.oases.service.WalletService
import javax.inject.Inject

class HomePresenter @Inject constructor(): BasePresenter<HomeView>() {
    @Inject
    lateinit var walkPointService: WalkPointService

    fun inquireWalkPoint() {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()

        walkPointService.inquireWalkPoint().execute(object : BaseSubscriber<InquireWalkPointResp>(mView) {
            override fun onNext(t: InquireWalkPointResp) {
                mView.onInquireWalkPoint(t)
            }
        }, lifecycleProvider)
    }
}