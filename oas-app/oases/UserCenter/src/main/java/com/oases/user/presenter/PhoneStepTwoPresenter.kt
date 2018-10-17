package com.oases.user.presenter

import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.user.data.protocol.CheckPhoneCodeResp
import com.oases.user.data.protocol.SendMobileResp
import com.oases.user.presenter.view.*
import com.oases.user.service.UserService
import javax.inject.Inject

class PhoneStepTwoPresenter @Inject constructor() : BasePresenter<CheckPhoneCodeView>() {

    @Inject
    lateinit var userService: UserService

    /*
        发送验证码
     */
    fun  sendMobile(mobile: String) {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        userService.sendMobile(mobile).execute(object : BaseSubscriber<SendMobileResp>(mView) {
            override fun onNext(t: SendMobileResp) {
                mView.onSendMobileResult(t)
            }

            override fun onError(e: Throwable) {
                mView.onSendMobileResult(SendMobileResp("false"))
                mView.hideLoading()
            }
        }, lifecycleProvider)
    }

    fun  checkPhoneCode(mobileCode: String) {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        userService.checkPhoneCode(mobileCode).execute(object : BaseSubscriber<CheckPhoneCodeResp>(mView) {
            override fun onNext(t: CheckPhoneCodeResp) {
                mView.onCheckPhoneCodeResult(t)
            }

            override fun onError(e: Throwable) {
                mView.onCheckPhoneCodeResult(CheckPhoneCodeResp("false"))
                mView.hideLoading()
            }
        }, lifecycleProvider)
    }

}