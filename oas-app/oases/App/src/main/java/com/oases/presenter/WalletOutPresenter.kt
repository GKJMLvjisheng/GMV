package com.oases.presenter

import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.computingpower.data.protocol.KYCVerifyStatusResp
import com.oases.data.protocol.TransferReq
import com.oases.presenter.view.WalletOutView
import com.oases.service.WalletService
import java.math.BigDecimal
import javax.inject.Inject

class WalletOutPresenter@Inject constructor(): BasePresenter<WalletOutView>() {
    @Inject
    lateinit var walletService: WalletService

    fun walletOutEvent(toUser:String,money:String,rank:String){
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()

        walletService.walletOutEvent(TransferReq(toUser,money,rank)).execute(object : BaseSubscriber<Int>(mView) {
            override fun onNext(t: Int) {
                mView.onGetWalletOutEvent(t)
            }
        }, lifecycleProvider)
    }

    //获取用户的日总额
    fun getDayMoneyTotal(){
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()

        walletService.getDayMoneyTotal().execute(object : BaseSubscriber<BigDecimal>(mView) {
            override fun onNext(t: BigDecimal) {
                mView.onGetDayMoneyTotal(t)
            }
        }, lifecycleProvider)
    }

    /***
     * 获取用户KYC状态
     */
    fun getKYCVerifyStatus(){
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        walletService.getKYCVerifyStatus().execute(object : BaseSubscriber<KYCVerifyStatusResp>(mView) {
            override fun onNext(t: KYCVerifyStatusResp) {
                mView.onGetKYCVerifyStatus(t)
            }
        }, lifecycleProvider)
    }

}