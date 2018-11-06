package com.oases.presenter

import com.oases.base.data.protocol.BaseResp
import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.data.protocol.*
import com.oases.presenter.view.ExchangePointsView
import com.oases.presenter.view.MyPointsView
import com.oases.presenter.view.RedrawOasView
import com.oases.service.WalletService
import io.reactivex.Observable
import retrofit2.http.Body
import java.math.BigDecimal
import javax.inject.Inject

class RedrawOasPresenter @Inject constructor() : BasePresenter<RedrawOasView>() {
    @Inject
    lateinit var walletService: WalletService
/*
    fun reDrawOas(period: String,
                    type:String,
                    factor:String,
                    amount:String){
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        walletService.redeemPoint(RedeemPointReq(period, 0, factor.toDouble(), amount.toDouble())).execute(object : BaseSubscriber<RedeemPointResp>(mView) {
            override fun onNext(t: RedeemPointResp) {
                mView.onRedrawResult()
            }
        }, lifecycleProvider)
    }*/
    fun withdraw(value:String,remark:String,extra:String){
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        walletService.withdraw(TransferReq("",value,remark,extra)).execute(object : BaseSubscriber<Int>(mView) {
            override fun onNext(t: Int) {
                mView.withdraw(t)
            }
        }, lifecycleProvider)
    }
    fun reverseWithdraw(amount: BigDecimal,gasPrice: BigDecimal,gasLimit: BigDecimal,remark:String){
        val req =  TransferOasReq("",amount,"",gasPrice, gasLimit, remark)
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        walletService.reverseWithdraw(req).execute(object : BaseSubscriber<Int>(mView) {
            override fun onNext(t: Int) {
                mView.reverseWithdraw(t)
            }
        }, lifecycleProvider)
    }
    fun getOasExtra(){
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        walletService.getOasExtra().execute(object : BaseSubscriber<OasResp>(mView) {
            override fun onNext(t: OasResp) {
                mView.getOasExtra(t)
            }
        }, lifecycleProvider)
    }
}