package com.oases.user.presenter

import android.util.Log
import com.oases.base.common.BaseConstant
import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.base.utils.AppPrefsUtils
import com.oases.user.data.protocol.UserInfo
import com.oases.user.data.protocol.confirmOldPwdReq
import com.oases.user.data.protocol.confirmOldPwdResp
import com.oases.user.presenter.view.CreateHelpWordView
import com.oases.user.presenter.view.NickNameView
import com.oases.user.presenter.view.SetPasswordView
import com.oases.user.service.UserService
import com.oases.user.utils.putUserInfo
import javax.inject.Inject

class CreateHelpWordPresenter @Inject constructor() : BasePresenter<CreateHelpWordView>() {

    @Inject
    lateinit var userService: UserService

    /*
        确认旧密码
     */
    fun  verifyPassword(userName: String,oldPwd:String) {
        val req = confirmOldPwdReq(userName,oldPwd)
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        userService.verifyPassword(req).execute(object : BaseSubscriber<confirmOldPwdResp>(mView) {
            override fun onNext(t: confirmOldPwdResp) {
                mView.onConfirmOldPwdResult(t)
            }

            override fun onError(e: Throwable) {
                mView.onConfirmOldPwdResult(confirmOldPwdResp("false"))
                mView.hideLoading()
            }
        }, lifecycleProvider)
    }
}