package com.oases.user.presenter

import android.util.Log
import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.user.data.protocol.ForgetPwdOneResp
import com.oases.user.data.protocol.SendMobileResp
import com.oases.user.presenter.view.*
import com.oases.user.service.UserService
import javax.inject.Inject

class ForgetPwdOnePresenter @Inject constructor() : BasePresenter<ForgetPwdOneView>() {

    @Inject
    lateinit var userService: UserService

    /*
        查找当前登录用户手机号是否存在
     */
    fun  inquireUserNameByMobile(mobile: String) {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        userService.inquireUserNameByMobile(mobile).execute(object : BaseSubscriber<ForgetPwdOneResp>(mView) {
            override fun onNext(t: ForgetPwdOneResp) {
                Log.d("lh",t.name)
                mView.onInquireUserNameByMobileResult(t)
            }

            override fun onError(e: Throwable) {
                Log.i("lh","1111")
               mView.onInquireUserNameByMobileResult(ForgetPwdOneResp("empty"))
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