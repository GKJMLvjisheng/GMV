package com.oases.presenter

import android.util.Log
import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.data.protocol.*
import com.oases.presenter.view.MyPointsView
import com.oases.service.WalletService
import com.oases.user.data.protocol.UserInfo
import com.oases.user.service.UserService
import com.oases.user.utils.putUserInfo
import javax.inject.Inject


class MyPointsPresenter @Inject constructor() : BasePresenter<MyPointsView>() {
    @Inject
    lateinit var walletService: WalletService

    fun inquireCurrentPeriodPoints(){
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        walletService.inquireCurrentPeriodPoints().execute(object : BaseSubscriber<CurrentPeriodPointsResp>(mView) {
            override fun onNext(t: CurrentPeriodPointsResp) {
                mView.onGetCurrentPeriodPoints(t)
            }
        }, lifecycleProvider)
    }

    fun inquirePoints() {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        walletService.inquirePoints().execute(object : BaseSubscriber<PointsInfo>(mView) {
            override fun onNext(t: PointsInfo) {
                mView.onGetPoints(t)
            }
        }, lifecycleProvider)
    }

    fun inquirePointsDetail(pageNumber:Int, size:Int){
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        var req = InquirePointsDetailReq(pageNumber, size)
        walletService.energyInOrOutDetail(req).execute(object : BaseSubscriber<InquireEnergyDetailResp>(mView) {
            override fun onNext(t: InquireEnergyDetailResp) {
              mView.onGetPointsDeails(t)
            }
        }, lifecycleProvider)
    }

    /***
     * 交易信息列表
     */
    fun energyDetail(pageNumber:Int, size:Int){
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()

        var req = InquirePointsDetailReq(pageNumber, size)
        walletService.energyInOrOutDetail(req).execute(object : BaseSubscriber<InquireEnergyDetailResp>(mView) {
            override fun onNext(t: InquireEnergyDetailResp) {
                mView.onGetTransactionDeails(t)
            }
        }, lifecycleProvider)

        var outFlag:Int = 0
        var outReq = InquireTransactionDetailReq(pageNumber, size, outFlag)

        //获取转出交易
        walletService.energyDetail(outReq).execute(object : BaseSubscriber<InquireEnergyDetailResp>(mView) {
            override fun onNext(t: InquireEnergyDetailResp) {
                mView.onGetTransactionOutDeails(t)
            }
        }, lifecycleProvider)

        //获取转入交易
        var inFlag:Int = 1
        var inReq = InquireTransactionDetailReq(pageNumber, size, inFlag)
        walletService.energyDetail(inReq).execute(object : BaseSubscriber<InquireEnergyDetailResp>(mView) {
            override fun onNext(t: InquireEnergyDetailResp) {
                mView.onGetTransactionInDeails(t)
            }
        }, lifecycleProvider)
    }
}