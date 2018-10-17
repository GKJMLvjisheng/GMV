package com.oases.computingpower.presenter

import android.util.Log
import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.computingpower.data.protocol.ComputingPowerResp
import com.oases.computingpower.data.protocol.MailStatusResp
import com.oases.computingpower.data.protocol.PhoneStatusResp
import com.oases.computingpower.data.protocol.PowerActivityStatusResp
import com.oases.computingpower.presenter.view.ComputingPowerMainView
import com.oases.computingpower.service.ComputingPowerService
import javax.inject.Inject

class ComputingPowerMainPresenter @Inject constructor() : BasePresenter<ComputingPowerMainView>() {
    @Inject
    lateinit var computingPowerService: ComputingPowerService

    fun inquireCurrentPeriodPoints() {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        computingPowerService.inquirePower().execute(object : BaseSubscriber<ComputingPowerResp>(mView) {
            override fun onNext(t: ComputingPowerResp) {
                mView.onGetPower(t)
            }
        }, lifecycleProvider)
    }

    /*
        查询用户是否已填写手机号
     */
    fun  checkPhone() {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        computingPowerService.checkPhone().execute(object : BaseSubscriber<PhoneStatusResp>(mView) {
            override fun onNext(t: PhoneStatusResp) {
                mView.onCheckPhoneResult(t)
            }

            /*override fun onError(e: Throwable) {
                Log.i("lh","checkPhone")
                mView.onCheckPhoneResult(PhoneStatusResp("empty"))
                mView.hideLoading()
            }*/
        }, lifecycleProvider)
    }

    /*
        查询用户是否已填写邮箱
     */
    fun  checkMail() {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        computingPowerService.checkMail().execute(object : BaseSubscriber<MailStatusResp>(mView) {
            override fun onNext(t: MailStatusResp) {
                mView.onCheckMailResult(t)
            }

            /*override fun onError(e: Throwable) {
                Log.i("lh","checkMail")
                mView.onCheckMailResult(MailStatusResp("empty"))
                mView.hideLoading()
            }*/
        }, lifecycleProvider)
    }

    fun  inquirePowerActivityStatus() {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        computingPowerService.inquirePowerActivityStatus().execute(object : BaseSubscriber<PowerActivityStatusResp>(mView) {
            override fun onNext(t: PowerActivityStatusResp) {
                mView.onInquirePowerActivityStatusResult(t)
            }
        }, lifecycleProvider)
    }
}