package com.oases.user.presenter

import android.util.Log
import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.user.data.protocol.SendMobileResp
import com.oases.user.presenter.view.*
import com.oases.user.service.UserService
import javax.inject.Inject

class PhoneStepOnePresenter @Inject constructor() : BasePresenter<PhoneStepOneView>() {

    @Inject
    lateinit var userService: UserService

    /*
        手机号查重
     */
    fun  checkPhone(mobile: String) {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        userService.checkPhone(mobile).execute(object : BaseSubscriber<Int>(mView) {
            override fun onNext(t: Int) {
                mView.onCheckPhoneResult(t)
            }

            override fun onError(e: Throwable) {
                Log.i("zbb","111111111111111111")
               mView.onCheckPhoneResult(1)
                mView.hideLoading()
            }
        }, lifecycleProvider)
    }

    /*
        发送验证码
     */
    /*fun  sendMobile(mobile: String) {
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
    }*/

}