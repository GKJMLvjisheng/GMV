package com.oases.presenter

import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.computingpower.data.protocol.KYCVerifyStatusResp
import com.oases.data.protocol.BalanceDetailResp

import com.oases.data.protocol.InquireAddressResp

import com.oases.data.protocol.InquirePointsDetailReq
import com.oases.data.protocol.InquirePointsDetailResp
import com.oases.data.protocol.InquireTransactionDetailReq

import com.oases.presenter.view.OnLineWalletView
import com.oases.service.WalletService
import javax.inject.Inject

class OnLineWalletPresenter @Inject constructor(): BasePresenter<OnLineWalletView>() {
    @Inject
    lateinit var walletService: WalletService

    fun inquireBalance() {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        walletService.inquireBalance().execute(object : BaseSubscriber<BalanceDetailResp>(mView) {
            override fun onNext(t: BalanceDetailResp) {
                mView.setBalance(t)
            }
        }, lifecycleProvider)
    }


    fun inquireAddress(){
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        walletService.inquireAddress().execute(object : BaseSubscriber<InquireAddressResp>(mView) {
            override fun onNext(t: InquireAddressResp) {
                mView.onInquireAddress(t)
            }
        }, lifecycleProvider)
    }

    fun transactionDetail(pageNumber:Int, size:Int) {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        var req = InquirePointsDetailReq(pageNumber, size)
        walletService.transactionInOrOutDetail(req).execute(object : BaseSubscriber<InquirePointsDetailResp>(mView) {
            override fun onNext(t: InquirePointsDetailResp) {
                mView.onGetTransactionDeails(t)
            }

        }, lifecycleProvider)
    }

    fun onWalletTransactionAll(req:InquirePointsDetailReq){
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        walletService.transactionInOrOutDetail(req).execute(object : BaseSubscriber<InquirePointsDetailResp>(mView) {
            override fun onNext(t: InquirePointsDetailResp) {
                mView.onGetTransactionMoreDeails(t)
            }
            override fun onError(e: Throwable) {
                super.onError(e)
            }
        }, lifecycleProvider)
    }
    //获取转出交易
    fun onWalletTransactionOut(outReq:InquireTransactionDetailReq){
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        walletService.transactionDetail(outReq).execute(object : BaseSubscriber<InquirePointsDetailResp>(mView) {
            override fun onNext(t: InquirePointsDetailResp) {
                mView.onGetTransactionOutDeails(t)
            }
            override fun onError(e: Throwable) {
                super.onError(e)
            }
        }, lifecycleProvider)
    }

    fun onWalletTransactionIn(inReq:InquireTransactionDetailReq){
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        walletService.transactionDetail(inReq).execute(object : BaseSubscriber<InquirePointsDetailResp>(mView) {
            override fun onNext(t: InquirePointsDetailResp) {
                mView.onGetTransactionInDeails(t)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
            }
        }, lifecycleProvider)
    }
    /***
     * 交易信息列表
     */
    fun transactionOnWalletDetail(pageNumber:Int, size:Int){
/*        if (!checkNetWork()) {
            return
        }
        mView.showLoading()*/
        var req = InquirePointsDetailReq(pageNumber, size)
        onWalletTransactionAll(req)

        var outFlag:Int = 0
        var outReq = InquireTransactionDetailReq(pageNumber, size, outFlag)
        onWalletTransactionOut(outReq)

        //获取转入交易
        var inFlag:Int = 1
        var inReq = InquireTransactionDetailReq(pageNumber, size, inFlag)
        onWalletTransactionIn(inReq)
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