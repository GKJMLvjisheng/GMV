package com.oases.presenter

import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.data.protocol.*
import com.oases.presenter.view.ExchangeDetailView
import com.oases.service.WalletService
import javax.inject.Inject

class ExchangeDetailPresenter @Inject constructor(): BasePresenter<ExchangeDetailView>() {
    @Inject
    lateinit var walletService: WalletService

    fun listCoin() {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        walletService.listCoin().execute(object : BaseSubscriber<ListCoinResp>(mView) {
            override fun onNext(t: ListCoinResp) {
                mView.setCoin(t)
            }
        }, lifecycleProvider)
    }

    fun onWalletTransactionAll(req:InquirePointsDetailReq,outReq:InquireTransactionDetailReq){
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
    fun onEnergyTransactionAll(req:InquirePointsDetailReq,outReq:InquireTransactionDetailReq){
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        walletService.energyInOrOutDetail(req).execute(object : BaseSubscriber<InquireEnergyDetailResp>(mView) {
            override fun onNext(t: InquireEnergyDetailResp) {
                mView.onGetEnergyMoreDeails(t)
                //mView.onGetTransactionMoreDeails(t)
            }
        }, lifecycleProvider)
    }
    fun onExchangeTransactionAll(req:InquirePointsDetailReq,outReq:InquireTransactionDetailReq){
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        walletService.exchangeInOrOutDetail(req).execute(object : BaseSubscriber<InquirePointsDetailResp>(mView) {
            override fun onNext(t: InquirePointsDetailResp) {
                mView.onGetTransactionMoreDeails(t)
            }
        }, lifecycleProvider)
    }
    //获取转出交易
    fun onWalletTransactionOut(req:InquirePointsDetailReq,outReq:InquireTransactionDetailReq){
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        walletService.transactionDetail(outReq).execute(object : BaseSubscriber<InquirePointsDetailResp>(mView) {
            override fun onNext(t: InquirePointsDetailResp) {
               //mView.onGetTransactionOutDeails(t)
                mView.onGetTransactionMoreDeails(t)
            }
            override fun onError(e: Throwable) {
                super.onError(e)
            }
        }, lifecycleProvider)
    }
    fun onEnergyTransactionOut(req:InquirePointsDetailReq,outReq:InquireTransactionDetailReq){
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        walletService.energyDetail(outReq).execute(object : BaseSubscriber<InquireEnergyDetailResp>(mView) {
            override fun onNext(t: InquireEnergyDetailResp) {
                //mView.onGetTransactionOutDeails(t)
                mView.onGetEnergyMoreDeails(t)
                //mView.onGetTransactionMoreDeails(t)
            }
        }, lifecycleProvider)
    }
    fun onExchangeTransactionOut(req:InquirePointsDetailReq,outReq:InquireTransactionDetailReq){
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        walletService.exchangeDetail(outReq).execute(object : BaseSubscriber<InquirePointsDetailResp>(mView) {
            override fun onNext(t: InquirePointsDetailResp) {
                //mView.onGetTransactionOutDeails(t)
                mView.onGetTransactionMoreDeails(t)
            }
            override fun onError(e: Throwable) {
                super.onError(e)
            }
        }, lifecycleProvider)
    }

    fun onWalletTransactionIn(req:InquirePointsDetailReq,inReq:InquireTransactionDetailReq){
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        walletService.transactionDetail(inReq).execute(object : BaseSubscriber<InquirePointsDetailResp>(mView) {
            override fun onNext(t: InquirePointsDetailResp) {
                //mView.onGetTransactionInDeails(t)
                mView.onGetTransactionMoreDeails(t)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
            }
        }, lifecycleProvider)
    }
    fun onEnergyTransactionIn(req:InquirePointsDetailReq,inReq:InquireTransactionDetailReq){
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        walletService.energyDetail(inReq).execute(object : BaseSubscriber<InquireEnergyDetailResp>(mView) {
            override fun onNext(t: InquireEnergyDetailResp) {
                //mView.onGetTransactionInDeails(t)
                mView.onGetEnergyMoreDeails(t)
                //mView.onGetTransactionMoreDeails(t)
            }
        }, lifecycleProvider)
    }
    fun onExchangeTransactionIn(req:InquirePointsDetailReq,inReq:InquireTransactionDetailReq){
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        walletService.exchangeDetail(inReq).execute(object : BaseSubscriber<InquirePointsDetailResp>(mView) {
            override fun onNext(t: InquirePointsDetailResp) {
                //mView.onGetTransactionInDeails(t)
                mView.onGetTransactionMoreDeails(t)
            }
        }, lifecycleProvider)
    }

    fun exchangeOasMoreDetail(req:InquirePointsDetailReq,outReq:InquireTransactionDetailReq,inReq:InquireTransactionDetailReq){
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        walletService.exchangeInOrOutDetail(req).execute(object : BaseSubscriber<InquirePointsDetailResp>(mView) {
            override fun onNext(t: InquirePointsDetailResp) {
                mView.onGetOasAllDeails(t)
            }
        }, lifecycleProvider)

        walletService.exchangeDetail(outReq).execute(object : BaseSubscriber<InquirePointsDetailResp>(mView) {
            override fun onNext(t: InquirePointsDetailResp) {
                mView.onGetTransactionOutDeails(t)
            }
        }, lifecycleProvider)

        walletService.exchangeDetail(inReq).execute(object : BaseSubscriber<InquirePointsDetailResp>(mView) {
            override fun onNext(t: InquirePointsDetailResp) {
                mView.onGetTransactionInDeails(t)
            }
        }, lifecycleProvider)
    }



}