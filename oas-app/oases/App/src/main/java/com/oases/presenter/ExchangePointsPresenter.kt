package com.oases.presenter

import com.oases.base.data.protocol.BaseResp
import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.data.protocol.*
import com.oases.presenter.view.ExchangePointsView
import com.oases.presenter.view.MyPointsView
import com.oases.service.WalletService
import io.reactivex.Observable
import retrofit2.http.Body
import javax.inject.Inject

class ExchangePointsPresenter @Inject constructor() : BasePresenter<ExchangePointsView>() {
    @Inject
    lateinit var walletService: WalletService

    fun inquirePointsFactor(date: String){
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        walletService.inquirePointsFactor(InquirePointFactorReq(date)).execute(object : BaseSubscriber<InquirePointFactorResp>(mView) {
            override fun onNext(t: InquirePointFactorResp) {
                mView.onGetFactor(t)
            }
        }, lifecycleProvider)
    }

    fun redeemPoint(period: String,
                    type:String,
                    factor:String,
                    amount:String){
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        walletService.redeemPoint(RedeemPointReq(period, 0, factor.toDouble(), amount.toDouble())).execute(object : BaseSubscriber<RedeemPointResp>(mView) {
            override fun onNext(t: RedeemPointResp) {
                mView.onRedeemSucceed()
            }
        }, lifecycleProvider)
    }
}