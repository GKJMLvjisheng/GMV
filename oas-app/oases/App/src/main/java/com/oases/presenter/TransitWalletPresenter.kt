package com.oases.presenter

import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.data.protocol.BalanceDetailResp
import com.oases.data.protocol.ListCoinResp
import com.oases.data.protocol.TransitWalletSummary
import com.oases.presenter.view.TransitWalletView
import com.oases.service.WalletService
import javax.inject.Inject

class TransitWalletPresenter @Inject constructor(): BasePresenter<TransitWalletView>() {
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

    fun summary() {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        walletService.summary().execute(object : BaseSubscriber<TransitWalletSummary>(mView) {
            override fun onNext(t: TransitWalletSummary) {
                mView.setSummary(t)
            }
        }, lifecycleProvider)
    }
}