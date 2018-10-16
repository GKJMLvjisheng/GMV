package com.oases.presenter

import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.data.protocol.*
import com.oases.presenter.view.ExchangeOutView
import com.oases.presenter.view.MainView
import com.oases.presenter.view.TransitWalletView
import com.oases.presenter.view.WalletOutView
import com.oases.service.WalletService
import java.math.BigDecimal
import javax.inject.Inject

class MainPresenter @Inject constructor(): BasePresenter<MainView>() {
    @Inject
    lateinit var walletService: WalletService

    fun onGetAppUpdate(){
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()

        walletService.getAppUpdate().execute(object : BaseSubscriber<AppUpdateResp>(mView) {
            override fun onNext(t: AppUpdateResp) {
                mView.onGetAppUpdate(t)
            }
        }, lifecycleProvider)
    }

}