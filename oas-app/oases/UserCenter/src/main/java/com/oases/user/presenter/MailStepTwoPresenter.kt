package com.oases.user.presenter

import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.user.data.protocol.CheckMailCodeResp
import com.oases.user.data.protocol.CheckPhoneCodeResp
import com.oases.user.data.protocol.SendMailResp
import com.oases.user.data.protocol.SendMobileResp
import com.oases.user.presenter.view.*
import com.oases.user.service.UserService
import javax.inject.Inject

class MailStepTwoPresenter @Inject constructor() : BasePresenter<CheckMailCodeView>() {

    @Inject
    lateinit var userService: UserService

    /*
        发送验证码
     */
    fun  sendMail(email: String) {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        userService.sendMail(email).execute(object : BaseSubscriber<SendMailResp>(mView) {
            override fun onNext(t: SendMailResp) {
                mView.onSendMailResult(t)
            }

            override fun onError(e: Throwable) {
                mView.onSendMailResult(SendMailResp("false"))
                mView.hideLoading()
            }
        }, lifecycleProvider)
    }

    fun  checkMailCode(mailCode: String) {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        userService.checkMailCode(mailCode).execute(object : BaseSubscriber<CheckMailCodeResp>(mView) {
            override fun onNext(t: CheckMailCodeResp) {
                mView.onCheckMailCodeResult(t)
            }

            override fun onError(e: Throwable) {
                mView.onCheckMailCodeResult(CheckMailCodeResp("false"))
                mView.hideLoading()
            }
        }, lifecycleProvider)
    }

}