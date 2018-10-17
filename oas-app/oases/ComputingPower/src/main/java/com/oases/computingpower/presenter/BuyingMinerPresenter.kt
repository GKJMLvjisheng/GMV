package com.oases.computingpower.presenter

import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.computingpower.data.protocol.ConfirmBuyMinerReq
import com.oases.computingpower.data.protocol.MinerInfoReq
import com.oases.computingpower.data.protocol.MinerInfoResp
import com.oases.computingpower.presenter.view.BuyingMinerView
import com.oases.computingpower.service.ComputingPowerService
import javax.inject.Inject


class BuyingMinerPresenter @Inject constructor() : BasePresenter<BuyingMinerView>() {
    @Inject
    lateinit var computingPowerService: ComputingPowerService


    fun getMinerInfo(pageNumber:Int,pageSize:Int){
        val req = MinerInfoReq(pageNumber,pageSize)
        if (!checkNetWork()){
            return
        }
        mView.showLoading()
        computingPowerService.getMinerInfo(req).execute(object :BaseSubscriber<MinerInfoResp>(mView){
            override fun onNext(t: MinerInfoResp) {
                mView.onGetMinerInfoResult(t)
            }
        },lifecycleProvider)
    }

    fun confirmBuyMiner(minerName:String,minerNum:Int,minerTotalAcount:String){
        val req = ConfirmBuyMinerReq(minerName,minerNum,minerTotalAcount)
        if (!checkNetWork()){
            return
        }
        mView.showLoading()
        computingPowerService.confirmBuyMiner(req).execute(object :BaseSubscriber<Int>(mView){
            override fun onNext(t: Int) {
                mView.onConfirmBuyMinerResult(t)
            }
        },lifecycleProvider)
    }

}