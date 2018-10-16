package com.oases.user.presenter

import com.oases.base.common.BaseConstant
import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.base.utils.AppPrefsUtils
import com.oases.user.data.protocol.CheckPhoneCodeResp
import com.oases.user.data.protocol.DonePhoneResp
import com.oases.user.data.protocol.SendMobileResp
import com.oases.user.data.protocol.UserInfo
import com.oases.user.presenter.view.*
import com.oases.user.service.UserService
import com.oases.user.utils.putUserInfo
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_user_info.*
import javax.inject.Inject

class ResetPwdPresenter @Inject constructor() : BasePresenter<ResetPwdView>() {

    @Inject
    lateinit var userService: UserService

    fun  resetPwd(name:String,newPwd: String) {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        userService.resetPwd(name,newPwd).execute(object : BaseSubscriber<Int>(mView) {
            override fun onNext(t: Int) {
                AppPrefsUtils.putString(BaseConstant.USER_PASSWORD, newPwd)
                mView.onResetPwdResult(t)
            }

            override fun onError(e: Throwable) {
                mView.onResetPwdResult(1)
                mView.hideLoading()
            }
        }, lifecycleProvider)
    }

}