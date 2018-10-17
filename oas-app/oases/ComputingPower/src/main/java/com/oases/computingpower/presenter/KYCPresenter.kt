package com.oases.computingpower.presenter

import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.computingpower.data.protocol.KYCImageUriReq
import com.oases.computingpower.data.protocol.KYCInfoResp
import com.oases.computingpower.data.protocol.KYCVerifyStatusResp
import com.oases.computingpower.presenter.view.KYCView
import com.oases.computingpower.service.ComputingPowerService
import okhttp3.MultipartBody
import javax.inject.Inject


class KYCPresenter @Inject constructor() : BasePresenter<KYCView>() {
    @Inject
    lateinit var computingPowerService: ComputingPowerService

    fun upLoadKYCInfo(KYCInfo: MultipartBody.Part) {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        computingPowerService.upLoadKYCInfo(KYCInfo).execute(object : BaseSubscriber<KYCInfoResp>(mView) {
            override fun onNext(t: KYCInfoResp) {
                mView.onUpLoadKYCInfoResult(t)
            }
        }, lifecycleProvider)
    }

    fun getKYCVerifyStatus(){
        if (!checkNetWork()){
            return
        }
        mView.showLoading()
        computingPowerService.getKYCVerifyStatus().execute(object :BaseSubscriber<KYCVerifyStatusResp>(mView){
            override fun onNext(t: KYCVerifyStatusResp) {
                mView.onGetKYCVerifyStatus(t)
            }
        },lifecycleProvider)
    }

    //用户点击提交按钮后更改KYC状态
    fun changeKYCStatus(frontIdUri:String ,backIdUri:String,handIdUri:String){
        val req = KYCImageUriReq(frontIdUri,backIdUri,handIdUri)
        if (!checkNetWork()){
            return
        }
        mView.showLoading()
        computingPowerService.changeKYCStatus(req).execute(object :BaseSubscriber<Int>(mView){
            override fun onNext(t: Int) {
                mView.onChangeKYCStatus(t)
            }
        },lifecycleProvider)
    }
}