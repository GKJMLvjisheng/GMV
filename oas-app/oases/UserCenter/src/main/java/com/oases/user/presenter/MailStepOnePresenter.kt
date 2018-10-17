package com.oases.user.presenter

import android.util.Log
import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.user.data.protocol.SendMailResp
import com.oases.user.data.protocol.SendMobileResp
import com.oases.user.presenter.view.*
import com.oases.user.service.UserService
import javax.inject.Inject

class MailStepOnePresenter @Inject constructor() : BasePresenter<MailStepOneView>() {

    @Inject
    lateinit var userService: UserService

    /*
        邮箱查重
     */
    fun  checkMail(email: String) {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        userService.checkMail(email).execute(object : BaseSubscriber<Int>(mView) {
            override fun onNext(t: Int) {
             //   mView.showLoading()
                mView.onCheckMailResult(t)
            }

            override fun onError(e: Throwable) {
                Log.i("zbb","111111111111111111")
               mView.onCheckMailResult(1)
                mView.hideLoading()
            }
        }, lifecycleProvider)
    }

    /*
        发送验证码
     */
    /*fun  sendMail(email: String) {
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
    }*/

}