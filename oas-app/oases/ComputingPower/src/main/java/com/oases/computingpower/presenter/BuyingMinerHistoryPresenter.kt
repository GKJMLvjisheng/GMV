package com.oases.computingpower.presenter

import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.computingpower.data.protocol.BuyingMinerHistoryReq
import com.oases.computingpower.data.protocol.BuyingMinerHistoryResp
import com.oases.computingpower.presenter.view.BuyingMinerHistoryView
import com.oases.computingpower.service.ComputingPowerService
import javax.inject.Inject


class BuyingMinerHistoryPresenter @Inject constructor() : BasePresenter<BuyingMinerHistoryView>() {
    @Inject
    lateinit var computingPowerService: ComputingPowerService


    fun getBuyingMinerHistory(pageNumber:Int,pageSize:Int){
        val req = BuyingMinerHistoryReq(pageNumber,pageSize)
        if (!checkNetWork()){
            return
        }
        mView.showLoading()
        computingPowerService.getBuyingMinerHistory(req).execute(object :BaseSubscriber<BuyingMinerHistoryResp>(mView){
            override fun onNext(t: BuyingMinerHistoryResp) {
                mView.onGetBuyingMinerHistoryResult(t)
            }
        },lifecycleProvider)
    }



}