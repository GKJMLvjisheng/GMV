package com.oases.presenter

import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.data.protocol.CurrentPeriodPointsResp
import com.oases.data.protocol.TransferOasReq
import com.oases.data.protocol.TransferOasResp
import com.oases.data.protocol.TransferReq
import com.oases.presenter.view.ExchangeOutView
import com.oases.presenter.view.TransitWalletView
import com.oases.presenter.view.WalletOutView
import com.oases.service.WalletService
import java.math.BigDecimal
import javax.inject.Inject

class ExchangeOutPresenter@Inject constructor(): BasePresenter<ExchangeOutView>() {
    @Inject
    lateinit var walletService: WalletService

    fun ExchangeOutEvent(toUserAddress: String,amount: BigDecimal,contract:String, gasPrice: BigDecimal,gasLimit: BigDecimal,comment:String){//changeAddress:String,
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()

        walletService.ExchangeOutEvent(TransferOasReq(toUserAddress,amount,contract, gasPrice,gasLimit,comment)).execute(object : BaseSubscriber<TransferOasResp>(mView) {//changeAddress,
            override fun onNext(t: TransferOasResp) {
                mView.onGetExchangeOutEvent(t)
            }
        }, lifecycleProvider)
    }

}